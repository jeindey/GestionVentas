package com.casosemestral.gestionventas.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(

    onGuardarClick: () -> Unit,

    onVolverClick: () -> Unit

) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Registro de Venta")
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

            Text(
                text = "Formulario de Venta"
            )

            Button(
                onClick = onGuardarClick
            ) {
                Text("Guardar")
            }

            Button(
                onClick = onVolverClick
            ) {
                Text("Volver")
            }
        }
    }
}