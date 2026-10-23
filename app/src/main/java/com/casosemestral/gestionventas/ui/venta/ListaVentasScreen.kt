package com.casosemestral.gestionventas.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.casosemestral.gestionventas.data.local.VentaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaVentasScreen(
    uiState: VentaUiState,
    onBuscarChange: (String) -> Unit,
    onNuevaVentaClick: () -> Unit,
    onEditarVentaClick: (Int) -> Unit,
    onEliminarClick: (VentaEntity) -> Unit,
    onConfirmarEliminar: () -> Unit,
    onCancelarEliminar: () -> Unit,
    onVolverClick: () -> Unit
) {
    // Diálogo de confirmación: se muestra solo si hay una venta pendiente de eliminar
    uiState.ventaAEliminar?.let { venta ->
        AlertDialog(
            onDismissRequest = onCancelarEliminar,
            title = { Text("Eliminar venta") },
            text = {
                Text("¿Eliminar la venta ${venta.codigoAutorizacion}? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(onClick = onConfirmarEliminar) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelarEliminar) {
                    Text("Cancelar")
                }
            }
        )
    }

    // El filtro ya viene calculado desde el UiState
    val ventasMostradas = uiState.ventasFiltradas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Ventas") },
                navigationIcon = {
                    TextButton(onClick = onVolverClick) { Text("Menú") }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onNuevaVentaClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nueva Venta")
            }

            OutlinedTextField(
                value = uiState.busqueda,
                onValueChange = onBuscarChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por código") },
                singleLine = true
            )

            Text(
                text = "Total ventas: ${ventasMostradas.size}",
                style = MaterialTheme.typography.labelLarge
            )

            if (ventasMostradas.isEmpty()) {
                Text("No existen ventas registradas")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ventasMostradas, key = { it.id }) { venta ->
                        VentaCard(
                            venta = venta,
                            onEditarClick = { onEditarVentaClick(venta.id) },
                            onEliminarClick = { onEliminarClick(venta) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VentaCard(
    venta: VentaEntity,
    onEditarClick: () -> Unit,
    onEliminarClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Código: ${venta.codigoAutorizacion}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Servicio: ${venta.tipoServicio}")
            Text("Cliente: ${venta.cliente}")
            Text("Fecha: ${venta.fechaOperacion}")
            Text("Monto: \$${venta.monto}")
            Text("Estado: ${venta.estado}")

            if (venta.respaldoUri != null) {
                Text("Respaldo: ${venta.respaldoUri}")
            }

            // Solo se puede consultar/modificar/eliminar mientras no esté validada
            if (venta.esEditable()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onEditarClick) {
                        Text("Editar")
                    }
                    OutlinedButton(
                        onClick = onEliminarClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                }
            } else {
                Text(
                    text = "Venta validada: no se puede modificar ni eliminar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}