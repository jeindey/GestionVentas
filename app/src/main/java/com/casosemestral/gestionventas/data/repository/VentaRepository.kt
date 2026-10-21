package com.casosemestral.gestionventas.data.repository

import com.casosemestral.gestionventas.data.local.VentaDao
import com.casosemestral.gestionventas.data.local.VentaEntity

class VentaRepository(
    private val dao: VentaDao
) {

    fun obtenerVentas() =
        dao.obtenerVentas()

    suspend fun insertarVenta(
        venta: VentaEntity
    ) {
        dao.insertarVenta(venta)
    }

    suspend fun actualizarVenta(
        venta: VentaEntity
    ) {
        dao.actualizarVenta(venta)
    }

    suspend fun existeCodigo(
        codigo: String
    ): Boolean {
        return dao.existeCodigo(codigo) > 0
    }

    suspend fun obtenerVentaPorId(
        id: Int
    ): VentaEntity? {

        return dao.obtenerVentaPorId(
            id
        )
    }

    suspend fun obtenerVentaPorCodigo(
        codigo: String
    ): VentaEntity? {

        return dao.obtenerVentaPorCodigo(
            codigo
        )
    }

}