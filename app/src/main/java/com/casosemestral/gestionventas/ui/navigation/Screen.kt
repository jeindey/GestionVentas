package com.casosemestral.gestionventas.ui.navigation

// Rutas tipadas (sealed class), como en la guia 10 de navegacion
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Menu : Screen("menu")
    data object ListaVentas : Screen("lista_ventas")
    data object VentaForm : Screen("venta_form")
    data object QrScanner : Screen("qr_scanner")
}