package com.casosemestral.gestionventas.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(

    uiState: VentaUiState,

    viewModel: VentaViewModel,

    onGuardarClick: () -> Unit,

    onVolverClick: () -> Unit

) {
    LaunchedEffect(
        uiState.guardadoExitosamente
    ) {

        if (uiState.guardadoExitosamente) {
            onGuardarClick()
            viewModel.limpiarGuardadoExitoso()
        }
    }

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
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = uiState.tipoServicio,
                onValueChange = {
                    viewModel.actualizarTipoServicio(it)
                },

                modifier = Modifier.fillMaxWidth(),

                isError = uiState.tipoServicioError != null,

                label = {
                    Text("Tipo de Servicio")
                },
                supportingText = {
                    Text(
                        uiState.tipoServicioError ?: ""
                    )
                }
            )

            OutlinedTextField(
                value = uiState.fechaOperacion,
                onValueChange = {
                    viewModel.actualizarFecha(it)
                },
                modifier = Modifier.fillMaxWidth(),

                isError = uiState.fechaError != null,

                label = {
                    Text("Fecha Operación")
                },

                supportingText = {
                    Text(
                        uiState.fechaError ?: ""
                    )
                }

            )

            OutlinedTextField(
                value = uiState.cliente,
                onValueChange = {
                    viewModel.actualizarCliente(it)
                },
                modifier = Modifier.fillMaxWidth(),

                isError = uiState.clienteError != null,

                label = {
                    Text("Cliente")
                },

                supportingText = {
                    Text(
                        uiState.clienteError ?: ""
                    )
                }
            )

            OutlinedTextField(
                value = uiState.monto,
                onValueChange = {
                    viewModel.actualizarMonto(it)
                },
                modifier = Modifier.fillMaxWidth(),

                isError = uiState.montoError != null,

                label = {
                    Text("Monto")
                },

                supportingText = {
                    Text(
                        uiState.montoError ?: ""
                    )
                }
            )

            OutlinedTextField(
                value = uiState.codigoAutorizacion,
                onValueChange = {
                    viewModel.actualizarCodigo(it)
                },
                modifier = Modifier.fillMaxWidth(),

                isError = uiState.codigoError != null,

                label = {
                    Text("Código Autorización")
                },

                supportingText = {
                    Text(
                        uiState.codigoError ?: ""
                    )
                }

            )

            OutlinedTextField(
                value = uiState.observaciones,
                onValueChange = {
                    viewModel.actualizarObservaciones(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Observaciones")
                }
            )

            Button(
                onClick = {

                    viewModel.guardarVenta()

                    //onGuardarClick()
                }
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