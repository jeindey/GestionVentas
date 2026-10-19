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
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

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

    val context = LocalContext.current

    val photoFile = remember {

        val fileName =
            "venta_${System.currentTimeMillis()}.jpg"

        File(
            context.cacheDir,
            fileName
        )
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.TakePicture()
        ) { success ->

            if (success) {

                viewModel.actualizarRespaldoUri(
                    photoUri.toString()
                )
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                cameraLauncher.launch(photoUri)
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

                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {

                            cameraLauncher.launch(
                                photoUri
                            )
                        }
                        else -> {
                            permissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        }
                    }
                }
            ) {
                Text("📷 Capturar Evidencia")
            }

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