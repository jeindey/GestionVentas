package com.casosemestral.gestionventas.ui.venta

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow(VentaUiState())

    val uiState: StateFlow<VentaUiState> =
        _uiState.asStateFlow()

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
        // Se implementará al conectar Room
    }

    fun guardarVenta() {

        if (!validarFormulario()) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                guardando = true
            )

        // Aquí después guardaremos en Room
    }

}