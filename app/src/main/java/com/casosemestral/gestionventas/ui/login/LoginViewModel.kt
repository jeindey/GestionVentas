package com.casosemestral.gestionventas.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private data class CuentaFicticia(val clave: String, val nombre: String)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Cuentas FICTICIAS solo para la primera entrega (no son datos reales)
    private val cuentasFicticias = mapOf(
        "vendedor1" to CuentaFicticia("ven123", "Vendedor Uno"),
        "vendedor2" to CuentaFicticia("ven456", "Vendedora Dos")
    )

    fun onUsuarioChange(valor: String) {
        _uiState.update {
            it.copy(usuario = valor, usuarioError = null, errorGeneral = null)
        }
    }

    fun onClaveChange(valor: String) {
        _uiState.update {
            it.copy(clave = valor, claveError = null, errorGeneral = null)
        }
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val usuarioError = if (estado.usuario.isBlank()) "Usuario obligatorio" else null
        val claveError = if (estado.clave.isBlank()) "Clave obligatoria" else null

        _uiState.update {
            it.copy(usuarioError = usuarioError, claveError = claveError)
        }
        return usuarioError == null && claveError == null
    }

    fun iniciarSesion() {
        if (!validarFormulario()) return

        val estado = _uiState.value
        val cuenta = cuentasFicticias[estado.usuario.trim().lowercase()]

        if (cuenta != null && cuenta.clave == estado.clave) {
            _uiState.update {
                it.copy(
                    autenticado = true,
                    nombreUsuario = cuenta.nombre,
                    errorGeneral = null
                )
            }
        } else {
            _uiState.update { it.copy(errorGeneral = "Usuario o clave incorrectos") }
        }
    }

    // La pantalla ya navego: se baja la bandera y se borra la clave escrita
    fun consumirAutenticacion() {
        _uiState.update { it.copy(autenticado = false, clave = "") }
    }

    fun cerrarSesion() {
        _uiState.value = LoginUiState()
    }
}