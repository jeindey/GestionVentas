package com.casosemestral.gestionventas.ui.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.casosemestral.gestionventas.data.repository.VentaRepository

class VentaViewModelFactory(
    private val repository: VentaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                VentaViewModel::class.java
            )
        ) {

            return VentaViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}