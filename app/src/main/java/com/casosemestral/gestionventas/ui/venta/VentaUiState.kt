package com.casosemestral.gestionventas.ui.venta

import com.casosemestral.gestionventas.data.local.VentaEntity

data class VentaUiState(

    // Lo que se está escribiendo en el formulario
    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val observaciones: String = "",
    // Respaldo: contenido del QR ficticio leído con la cámara
    val respaldoUri: String? = null,
    val estado: String = ESTADO_BORRADOR,

    // Errores por campo (null = campo válido)
    val tipoServicioError: String? = null,
    val fechaError: String? = null,
    val clienteError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,
    val errorGeneral: String? = null,

    // Datos de la lista
    val ventas: List<VentaEntity> = emptyList(),
    val busqueda: String = "",
    val ventaSeleccionadaId: Int? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
){
    // La lógica de filtrado vive en el estado, no en la pantalla
    val ventasFiltradas: List<VentaEntity>
        get() = ventas.filter {
            it.codigoAutorizacion.contains(busqueda.trim(), ignoreCase = true)
        }
}