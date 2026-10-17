package com.casosemestral.gestionventas.ui.venta

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
                tipoServicio = valor
            )
    }

    fun actualizarFecha(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                fechaOperacion = valor
            )
    }

    fun actualizarCliente(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                cliente = valor
            )
    }

    fun actualizarMonto(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                monto = valor
            )
    }

    fun actualizarCodigo(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                codigoAutorizacion = valor
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

        return valido
    }
}