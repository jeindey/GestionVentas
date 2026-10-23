package com.casosemestral.gestionventas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

@Dao
interface VentaDao {

    @Insert
    suspend fun insertarVenta(
        venta: VentaEntity
    )

    @Update
    suspend fun actualizarVenta(
        venta: VentaEntity
    )

    @Delete
    suspend fun eliminarVenta(
        venta: VentaEntity
    )

    @Query(
        """
        SELECT * FROM ventas
        ORDER BY id DESC
        """
    )
    fun obtenerVentas(): Flow<List<VentaEntity>>

    @Query(
        """
        SELECT * FROM ventas
        WHERE id = :id
        """
    )
    suspend fun obtenerVentaPorId(
        id: Int
    ): VentaEntity?

    @Query(
        """
        SELECT COUNT(*)
        FROM ventas
        WHERE UPPER(codigoAutorizacion) = UPPER(:codigo)
        """
    )
    suspend fun existeCodigo(
        codigo: String
    ): Int

    @Query(
        """
    SELECT *
    FROM ventas
    WHERE UPPER(codigoAutorizacion) =
          UPPER(:codigo)
    LIMIT 1
    """
    )
    suspend fun obtenerVentaPorCodigo(
        codigo: String
    ): VentaEntity?


}