package com.casosemestral.gestionventas.ui.venta

data class VentaUiState(

    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val observaciones: String = "",
    val respaldoUri: String? = null,

    val estado: String = "Borrador",

    val tipoServicioError: String? = null,
    val fechaError: String? = null,
    val clienteError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)