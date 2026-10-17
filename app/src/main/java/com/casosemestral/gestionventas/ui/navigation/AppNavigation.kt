package com.casosemestral.gestionventas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casosemestral.gestionventas.ui.venta.ListaVentasScreen
import com.casosemestral.gestionventas.ui.venta.VentaFormScreen

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

            VentaFormScreen(

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