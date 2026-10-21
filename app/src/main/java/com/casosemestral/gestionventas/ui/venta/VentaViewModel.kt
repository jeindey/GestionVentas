package com.casosemestral.gestionventas.ui.venta

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.casosemestral.gestionventas.data.local.VentaEntity
import com.casosemestral.gestionventas.data.repository.VentaRepository

class VentaViewModel(
    private val repository: VentaRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(VentaUiState())

    val uiState: StateFlow<VentaUiState> =
        _uiState.asStateFlow()

    init {
        cargarVentas()
    }

    fun actualizarTipoServicio(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                tipoServicio = valor,
                tipoServicioError = null
            )
    }

    fun actualizarFecha(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                fechaOperacion = valor,
                fechaError = null
            )
    }

    fun actualizarCliente(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                cliente = valor,
                clienteError = null
            )
    }

    fun actualizarMonto(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                monto = valor,
                montoError = null
            )
    }

    fun actualizarCodigo(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                codigoAutorizacion = valor,
                codigoError = null

            )
    }

    fun actualizarObservaciones(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                observaciones = valor
            )
    }

    fun validarFormulario(): Boolean {

        var valido = true

        if (_uiState.value.tipoServicio.isBlank()) {
            valido = false

            _uiState.value =
                _uiState.value.copy(
                    tipoServicioError =
                        "Debe seleccionar un servicio"
                )
        }
        if (_uiState.value.fechaOperacion.isBlank()) {

            valido = false

            _uiState.value =
                _uiState.value.copy(
                    fechaError =
                        "Fecha obligatoria"
                )
        }

        if (_uiState.value.cliente.isBlank()) {

            valido = false

            _uiState.value =
                _uiState.value.copy(
                    clienteError =
                        "Cliente obligatorio"
                )
        }

        val valorMonto =
            _uiState.value.monto.toDoubleOrNull()

        if (
            valorMonto == null ||
            valorMonto <= 0
        ) {

            valido = false

            _uiState.value =
                _uiState.value.copy(
                    montoError =
                        "Monto debe ser mayor a 0"
                )
        }
        if (_uiState.value.codigoAutorizacion.isBlank()) {

            valido = false

            _uiState.value =
                _uiState.value.copy(
                    codigoError =
                        "Código obligatorio"
                )
        }

        return valido
    }

    fun cargarVentas() {

        viewModelScope.launch {

            repository.obtenerVentas()
                .collect { lista ->

                    _uiState.value =
                        _uiState.value.copy(
                            ventas = lista
                        )
                }
        }
    }

    fun guardarVenta() {

        if (!validarFormulario()) {
            return
        }

        val codigoNormalizado =
            _uiState.value.codigoAutorizacion
                .trim()
                .uppercase()

        val nuevaVenta = VentaEntity(

            tipoServicio = _uiState.value.tipoServicio,
            fechaOperacion = _uiState.value.fechaOperacion,
            cliente = _uiState.value.cliente,
            monto = _uiState.value.monto.toDouble(),
            codigoAutorizacion =
                codigoNormalizado,
            observaciones =
                _uiState.value.observaciones,
            respaldoUri =
                _uiState.value.respaldoUri,
            estado = "Borrador"
        )

        viewModelScope.launch {

            val ventaConMismoCodigo =
                repository.obtenerVentaPorCodigo(
                    codigoNormalizado
                )

            if (
                ventaConMismoCodigo != null &&
                ventaConMismoCodigo.id !=
                _uiState.value.ventaSeleccionadaId
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        codigoError =
                            "Código ya registrado"
                    )

                return@launch
            }

            if (
                _uiState.value.ventaSeleccionadaId != null
            ) {

                repository.actualizarVenta(

                    nuevaVenta.copy(

                        id =
                            _uiState.value.ventaSeleccionadaId!!
                    )
                )

            } else {

                repository.insertarVenta(
                    nuevaVenta
                )
            }

            _uiState.value =
                _uiState.value.copy(
                    tipoServicio = "",
                    fechaOperacion = "",
                    cliente = "",
                    monto = "",
                    codigoAutorizacion = "",
                    observaciones = "",

                    tipoServicioError = null,
                    fechaError = null,
                    clienteError = null,
                    montoError = null,
                    codigoError = null,
                    respaldoUri = null,
                    ventaSeleccionadaId = null,

                    guardadoExitosamente = true
                )
        }
    }

    fun limpiarGuardadoExitoso() {

        _uiState.value =
            _uiState.value.copy(
                guardadoExitosamente = false
            )
    }

    fun actualizarRespaldoUri(
        uri: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                respaldoUri = uri
            )
    }

    fun seleccionarVenta(
        id: Int
    ) {

        _uiState.value =
            _uiState.value.copy(
                ventaSeleccionadaId = id
            )
    }

    fun cargarVentaParaEditar(
        id: Int
    ) {

        viewModelScope.launch {

            val venta =
                repository.obtenerVentaPorId(
                    id
                )

            venta?.let {

                _uiState.value =
                    _uiState.value.copy(

                        ventaSeleccionadaId =
                            it.id,

                        tipoServicio =
                            it.tipoServicio,

                        fechaOperacion =
                            it.fechaOperacion,

                        cliente =
                            it.cliente,

                        monto =
                            it.monto.toString(),

                        codigoAutorizacion =
                            it.codigoAutorizacion,

                        observaciones =
                            it.observaciones,

                        respaldoUri =
                            it.respaldoUri,

                        estado =
                            it.estado
                    )
            }
        }
    }

}