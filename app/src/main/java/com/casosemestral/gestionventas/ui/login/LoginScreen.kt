package com.casosemestral.gestionventas.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: () -> Unit
) {
    // La View observa el estado del ViewModel
    val estado by viewModel.uiState.collectAsState()

    // Cuando el ViewModel confirma el login, la View navega
    LaunchedEffect(estado.autenticado) {
        if (estado.autenticado) {
            onLoginExitoso()
            viewModel.consumirAutenticacion()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de Ventas") })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = estado.usuario,
                onValueChange = viewModel::onUsuarioChange,
                modifier = Modifier.fillMaxWidth(),
                isError = estado.usuarioError != null,
                label = { Text("Usuario") },
                supportingText = { Text(estado.usuarioError ?: "") }
            )

            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                modifier = Modifier.fillMaxWidth(),
                isError = estado.claveError != null,
                label = { Text("Clave") },
                visualTransformation = PasswordVisualTransformation(),
                supportingText = { Text(estado.claveError ?: "") }
            )

            estado.errorGeneral?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = { viewModel.iniciarSesion() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Text(
                text = "Cuenta de prueba: vendedor1 / ven123",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}