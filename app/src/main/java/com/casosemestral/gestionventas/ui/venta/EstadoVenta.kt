package com.casosemestral.gestionventas.ui.venta

import com.casosemestral.gestionventas.data.local.VentaEntity

const val ESTADO_BORRADOR = "Borrador"
const val ESTADO_PENDIENTE = "Pendiente de revisión"
const val ESTADO_VALIDADA = "Validada"

// Regla del caso: una venta solo se puede modificar mientras no esté validada
fun VentaEntity.esEditable(): Boolean = estado != ESTADO_VALIDADA