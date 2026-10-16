package com.casosemestral.gestionventas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ventas")
data class VentaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipoServicio: String,
    val fechaOperacion: String,
    val cliente: String,
    val monto: Double,
    val codigoAutorizacion: String,
    val observaciones: String,
    val respaldoUri: String?,
    val estado: String
)