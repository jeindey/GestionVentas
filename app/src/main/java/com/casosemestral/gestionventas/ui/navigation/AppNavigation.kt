package com.casosemestral.gestionventas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casosemestral.gestionventas.ui.venta.ListaVentasScreen
import com.casosemestral.gestionventas.ui.venta.VentaFormScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casosemestral.gestionventas.ui.venta.VentaViewModel
import androidx.compose.ui.platform.LocalContext
import com.casosemestral.gestionventas.data.local.AppDatabase
import com.casosemestral.gestionventas.data.repository.VentaRepository
import com.casosemestral.gestionventas.ui.venta.VentaViewModelFactory


@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val context = LocalContext.current

    val database =
        AppDatabase.getDatabase(
            context
        )

    val repository =
        VentaRepository(
            database.ventaDao()
        )

    val factory =
        VentaViewModelFactory(
            repository
        )

    val viewModel: VentaViewModel =
        viewModel(
            factory = factory
        )

    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "lista_ventas"
    ) {

        composable("lista_ventas") {

            ListaVentasScreen(
                uiState = uiState,

                onNuevaVentaClick = {
                    navController.navigate(
                        "venta_form"
                    )
                },

                onEditarVentaClick = { id ->

                    viewModel.cargarVentaParaEditar(
                        id
                    )

                    navController.navigate(
                        "venta_form"
                    )
                }
            )
        }

        composable("venta_form") {

            VentaFormScreen(
                uiState = uiState,
                viewModel = viewModel,
                onGuardarClick = {
                    navController.popBackStack()
                },
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}