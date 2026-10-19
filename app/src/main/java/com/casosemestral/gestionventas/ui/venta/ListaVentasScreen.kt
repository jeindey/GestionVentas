package com.casosemestral.gestionventas.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.casosemestral.gestionventas.ui.venta.VentaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaVentasScreen(

    uiState: VentaUiState,

    onNuevaVentaClick: () -> Unit

) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lista de Ventas")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = onNuevaVentaClick
            ) {
                Text("Nueva Venta")
            }

            if (uiState.ventas.isEmpty()) {
                Text(
                    text = "No existen ventas registradas"
                )

            } else {
                LazyColumn {
                    items(uiState.ventas) { venta ->
                        Text(
                            text =
                                "Id: ${venta.id}"
                        )
                        Text(
                            text =
                                "Servicio: ${venta.tipoServicio}"
                        )
                        Text(
                            text =
                                "Fecha: ${venta.fechaOperacion}"
                        )
                        Text(
                            text =
                                "Cliente: ${venta.cliente}"
                        )
                        Text(
                            text =
                                "Monto: ${venta.monto}"
                        )
                        Text(
                            text =
                                "Código: ${venta.codigoAutorizacion}"
                        )
                        Text(
                            text =
                                "Observaciones: ${venta.observaciones}"
                        )
                        Text(
                            text =
                                "Estado: ${venta.estado}"
                        )
                        Text("--------------")
                    }
                }
            }
        }
    }
}