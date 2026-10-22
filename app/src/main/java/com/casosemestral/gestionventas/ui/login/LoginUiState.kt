package com.casosemestral.gestionventas.ui.login

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val usuarioError: String? = null,
    val claveError: String? = null,
    val errorGeneral: String? = null,
    val nombreUsuario: String = "",
    val autenticado: Boolean = false
)