package com.casosemestral.gestionventas.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(
    uiState: VentaUiState,
    viewModel: VentaViewModel,
    onGuardarClick: () -> Unit,
    onEscanearQrClick: () -> Unit,
    onVolverClick: () -> Unit
) {
    // Cuando el ViewModel confirma el guardado, la pantalla vuelve a la lista
    LaunchedEffect(uiState.guardadoExitosamente) {
        if (uiState.guardadoExitosamente) {
            onGuardarClick()
            viewModel.limpiarGuardadoExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.ventaSeleccionadaId == null) "Nueva venta"
                        else "Editar venta"
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolverClick) { Text("Volver") }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                value = uiState.tipoServicio,
                onValueChange = viewModel::actualizarTipoServicio,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.tipoServicioError != null,
                label = { Text("Tipo de convenio o servicio") },
                supportingText = { Text(uiState.tipoServicioError ?: "") }
            )

            OutlinedTextField(
                value = uiState.fechaOperacion,
                onValueChange = viewModel::actualizarFecha,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.fechaError != null,
                label = { Text("Fecha de operación (dd/MM/aaaa)") },
                supportingText = { Text(uiState.fechaError ?: "") }
            )

            OutlinedTextField(
                value = uiState.cliente,
                onValueChange = viewModel::actualizarCliente,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.clienteError != null,
                label = { Text("Cliente (ficticio)") },
                supportingText = { Text(uiState.clienteError ?: "") }
            )

            OutlinedTextField(
                value = uiState.monto,
                onValueChange = viewModel::actualizarMonto,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.montoError != null,
                label = { Text("Monto") },
                supportingText = { Text(uiState.montoError ?: "") }
            )

            OutlinedTextField(
                value = uiState.codigoAutorizacion,
                onValueChange = viewModel::actualizarCodigo,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.codigoError != null,
                label = { Text("Código de autorización") },
                supportingText = { Text(uiState.codigoError ?: "") }
            )

            // Recurso nativo: la cámara lee un QR ficticio con el código de autorización
            OutlinedButton(
                onClick = onEscanearQrClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear QR del código de autorización")
            }

            if (uiState.respaldoUri != null) {
                Text(
                    text = "Respaldo adjunto → ${uiState.respaldoUri}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.observaciones,
                onValueChange = viewModel::actualizarObservaciones,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Observaciones") }
            )

            Text(
                text = "Estado: ${uiState.estado}",
                style = MaterialTheme.typography.labelLarge
            )

            uiState.errorGeneral?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.guardarVenta() },
                    enabled = !uiState.guardando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                Button(
                    onClick = { viewModel.enviarRevision() },
                    enabled = !uiState.guardando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Enviar a revisión")
                }
            }
        }
    }
}