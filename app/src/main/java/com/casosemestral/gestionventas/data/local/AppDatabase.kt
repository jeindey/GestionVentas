package com.casosemestral.gestionventas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VentaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ventaDao(): VentaDao

}