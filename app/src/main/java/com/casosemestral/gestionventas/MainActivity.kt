package com.casosemestral.gestionventas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.casosemestral.gestionventas.ui.navigation.AppNavigation
import com.casosemestral.gestionventas.ui.theme.GestionVentasTheme
import com.casosemestral.gestionventas.utils.CameraPermissionHelper

class MainActivity : ComponentActivity() {

    // Estado observable por Compose: indica si el permiso de camara esta concedido
    private var hasCameraPermission by mutableStateOf(false)

    // Registro para solicitar el permiso de camara al sistema
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Se necesita permiso de cámara para escanear el QR",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verificar permiso inicial
        hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)

        setContent {
            GestionVentasTheme {
                AppNavigation(
                    hasCameraPermission = hasCameraPermission,
                    onRequestPermission = {
                        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // El usuario pudo conceder/revocar el permiso desde los ajustes del sistema
        hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)
    }
}