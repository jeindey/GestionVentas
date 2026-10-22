package com.casosemestral.gestionventas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casosemestral.gestionventas.ui.login.LoginScreen
import com.casosemestral.gestionventas.ui.login.LoginViewModel
import com.casosemestral.gestionventas.ui.menu.MenuScreen
import com.casosemestral.gestionventas.ui.venta.ListaVentasScreen
import com.casosemestral.gestionventas.ui.venta.QrScannerScreen
import com.casosemestral.gestionventas.ui.venta.VentaFormScreen
import com.casosemestral.gestionventas.ui.venta.VentaViewModel

// Flujo: Login -> Menu -> ListaVentasScreen -> VentaFormScreen (-> QrScannerScreen)
@Composable
fun AppNavigation(
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val navController = rememberNavController()

    // Un unico ViewModel de ventas compartido por lista, formulario y escaner
    val loginViewModel: LoginViewModel = viewModel()
    val ventaViewModel: VentaViewModel = viewModel()

    val loginState by loginViewModel.uiState.collectAsState()
    val ventaState by ventaViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Menu.route) {
            MenuScreen(
                nombreUsuario = loginState.nombreUsuario,
                onRegistroVentasClick = {
                    navController.navigate(Screen.ListaVentas.route)
                },
                onCerrarSesionClick = {
                    loginViewModel.cerrarSesion()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ListaVentas.route) {
            ListaVentasScreen(
                uiState = ventaState,
                onBuscarChange = { ventaViewModel.actualizarBusqueda(it) },
                onNuevaVentaClick = {
                    // Limpia cualquier dato que haya quedado en el formulario
                    ventaViewModel.iniciarNuevaVenta()
                    navController.navigate(Screen.VentaForm.route)
                },
                onEditarVentaClick = { id ->
                    ventaViewModel.cargarVentaParaEditar(id)
                    navController.navigate(Screen.VentaForm.route)
                },
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable(Screen.VentaForm.route) {
            VentaFormScreen(
                uiState = ventaState,
                viewModel = ventaViewModel,
                onGuardarClick = { navController.popBackStack() },
                onEscanearQrClick = { navController.navigate(Screen.QrScanner.route) },
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable(Screen.QrScanner.route) {
            QrScannerScreen(
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = onRequestPermission,
                onQrDetected = { contenido ->
                    ventaViewModel.onQrDetectado(contenido)
                    navController.popBackStack()
                },
                onVolverClick = { navController.popBackStack() }
            )
        }
    }
}