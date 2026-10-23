package com.casosemestral.gestionventas.ui.venta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.casosemestral.gestionventas.data.local.AppDatabase
import com.casosemestral.gestionventas.data.local.VentaEntity
import com.casosemestral.gestionventas.data.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

// ViewModel -> Repository -> DAO -> Room (la pantalla nunca toca la base de datos)
class VentaViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = VentaRepository(
        AppDatabase.getDatabase(app).ventaDao()
    )

    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()
    // Formato de fecha estricto: dd/MM/aaaa (rechaza 31/02/2025, etc.)
    private val formatoFecha = DateTimeFormatter
        .ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    private fun fechaValida(texto: String): Boolean {
        return try {
            LocalDate.parse(texto, formatoFecha)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    init {
        cargarVentas()
    }

    // ---------- Eventos del formulario ----------

    fun actualizarTipoServicio(valor: String) {
        _uiState.update { it.copy(tipoServicio = valor, tipoServicioError = null) }
    }

    fun actualizarFecha(valor: String) {
        _uiState.update { it.copy(fechaOperacion = valor, fechaError = null) }
    }

    fun actualizarCliente(valor: String) {
        _uiState.update { it.copy(cliente = valor, clienteError = null) }
    }

    fun actualizarMonto(valor: String) {
        _uiState.update { it.copy(monto = valor, montoError = null) }
    }

    fun actualizarCodigo(valor: String) {
        _uiState.update { it.copy(codigoAutorizacion = valor, codigoError = null) }
    }

    fun actualizarObservaciones(valor: String) {
        _uiState.update { it.copy(observaciones = valor) }
    }

    fun actualizarBusqueda(valor: String) {
        _uiState.update { it.copy(busqueda = valor) }
    }

    // El escáner leyó un QR ficticio: se usa como código de autorización y como respaldo
    fun onQrDetectado(contenido: String) {
        val codigo = contenido.trim()
        _uiState.update {
            it.copy(
                codigoAutorizacion = codigo,
                codigoError = null,
                respaldoUri = "QR: $codigo"
            )
        }
    }

    // ---------- Validaciones mínimas ----------

    fun validarFormulario(): Boolean {
        val actual = _uiState.value
        // Acepta coma o punto decimal ("1500,5" o "1500.5")
        val monto = actual.monto.trim().replace(',', '.').toDoubleOrNull()

        val tipoError =
            if (actual.tipoServicio.isBlank()) "Debe indicar el tipo de convenio o servicio" else null

        val fechaError = when {
            actual.fechaOperacion.isBlank() -> "Fecha obligatoria"
            !fechaValida(actual.fechaOperacion.trim()) -> "Formato inválido (use dd/MM/aaaa)"
            else -> null
        }

        val clienteError =
            if (actual.cliente.isBlank()) "Cliente obligatorio" else null

        val montoError = when {
            actual.monto.isBlank() -> "Monto obligatorio"
            monto == null || monto.isNaN() || monto.isInfinite() -> "El monto debe ser numérico"
            monto <= 0 -> "El monto debe ser mayor que 0"
            else -> null
        }

        val codigoError =
            if (actual.codigoAutorizacion.isBlank()) "Código obligatorio" else null

        _uiState.update {
            it.copy(
                tipoServicioError = tipoError,
                fechaError = fechaError,
                clienteError = clienteError,
                montoError = montoError,
                codigoError = codigoError
            )
        }

        return listOf(tipoError, fechaError, clienteError, montoError, codigoError)
            .all { it == null }
    }

    // ---------- Lista ----------

    private fun cargarVentas() {
        viewModelScope.launch {
            // Flow de Room: la lista se actualiza sola al insertar o modificar
            repository.obtenerVentas().collect { lista ->
                _uiState.update { it.copy(ventas = lista) }
            }
        }
    }

    // Deja el formulario vacío (se usa al pulsar "Nueva Venta")
    fun iniciarNuevaVenta() {
        _uiState.update {
            VentaUiState(ventas = it.ventas, busqueda = it.busqueda)
        }
    }

    fun cargarVentaParaEditar(id: Int) {
        iniciarNuevaVenta()
        viewModelScope.launch {
            val venta = repository.obtenerVentaPorId(id) ?: return@launch

            // Una venta validada no se puede modificar
            if (!venta.esEditable()) {
                _uiState.update {
                    it.copy(errorGeneral = "Una venta validada no se puede modificar")
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    ventaSeleccionadaId = venta.id,
                    tipoServicio = venta.tipoServicio,
                    fechaOperacion = venta.fechaOperacion,
                    cliente = venta.cliente,
                    monto = venta.monto.toString(),
                    codigoAutorizacion = venta.codigoAutorizacion,
                    observaciones = venta.observaciones,
                    respaldoUri = venta.respaldoUri,
                    estado = venta.estado
                )
            }
        }
    }

    // ---------- Guardado ----------

    // Guarda manteniendo el estado actual (Borrador por defecto)
    fun guardarVenta() {
        guardar(_uiState.value.estado)
    }

    // Primero valida; el estado cambia solo si la venta realmente se guarda
    fun enviarRevision() {
        guardar(ESTADO_PENDIENTE)
    }

    private fun guardar(estadoFinal: String) {
        if (!validarFormulario()) return

        val actual = _uiState.value
        val idEditando = actual.ventaSeleccionadaId
        val codigoNormalizado = actual.codigoAutorizacion.trim().uppercase()

        val venta = VentaEntity(
            id = idEditando ?: 0,
            tipoServicio = actual.tipoServicio.trim(),
            fechaOperacion = actual.fechaOperacion.trim(),
            cliente = actual.cliente.trim(),
            monto = actual.monto.trim().replace(',', '.').toDouble(),
            codigoAutorizacion = codigoNormalizado,
            observaciones = actual.observaciones.trim(),
            respaldoUri = actual.respaldoUri,
            estado = estadoFinal
        )

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, errorGeneral = null) }

            // Código de autorización no duplicado (se ignora la propia venta al editar)
            val ventaConMismoCodigo = repository.obtenerVentaPorCodigo(codigoNormalizado)
            if (ventaConMismoCodigo != null && ventaConMismoCodigo.id != idEditando) {
                _uiState.update {
                    it.copy(codigoError = "Código ya registrado", guardando = false)
                }
                return@launch
            }

            if (idEditando != null) {
                val original = repository.obtenerVentaPorId(idEditando)
                if (original != null && !original.esEditable()) {
                    _uiState.update {
                        it.copy(
                            errorGeneral = "Una venta validada no se puede modificar",
                            guardando = false
                        )
                    }
                    return@launch
                }
                repository.actualizarVenta(venta)
            } else {
                repository.insertarVenta(venta)
            }

            // Formulario limpio y aviso a la pantalla para que vuelva a la lista
            _uiState.update {
                VentaUiState(
                    ventas = it.ventas,
                    busqueda = it.busqueda,
                    guardadoExitosamente = true
                )
            }
        }
    }

    // ---------- ELIMINAR ----------

    fun solicitarEliminar(venta: VentaEntity) {
        _uiState.update { it.copy(ventaAEliminar = venta) }
    }

    fun cancelarEliminar() {
        _uiState.update { it.copy(ventaAEliminar = null) }
    }

    fun confirmarEliminar() {
        val venta = _uiState.value.ventaAEliminar ?: return

        viewModelScope.launch {
            // Se vuelve a consultar la venta: solo se elimina si sigue siendo editable
            val actual = repository.obtenerVentaPorId(venta.id)
            if (actual != null && actual.esEditable()) {
                repository.eliminarVenta(actual)
            }
            _uiState.update { it.copy(ventaAEliminar = null) }
        }
    }

    fun limpiarGuardadoExitoso() {
        _uiState.update { it.copy(guardadoExitosamente = false) }
    }
}