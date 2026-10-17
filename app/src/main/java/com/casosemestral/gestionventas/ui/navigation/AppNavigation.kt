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


@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "lista_ventas"
    ) {

        composable("lista_ventas") {

            ListaVentasScreen(

                onNuevaVentaClick = {

                    navController.navigate(
                        "venta_form"
                    )
                }
            )
        }

        composable("venta_form") {

            val viewModel: VentaViewModel = viewModel()

            val uiState by viewModel.uiState.collectAsState()


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