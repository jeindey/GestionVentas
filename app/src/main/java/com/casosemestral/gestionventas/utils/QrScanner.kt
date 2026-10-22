package com.casosemestral.gestionventas.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "QrScanner"

/**
 * Componente reutilizable: muestra la vista previa de la camara y detecta codigos QR
 * con CameraX + ML Kit. Llama a onQrCodeScanned una sola vez con el contenido leido.
 */
@SuppressLint("UnsafeOptInUsageError")
@Composable
fun QrScanner(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Mantener actualizado el callback sin reiniciar CameraX
    val currentCallback by rememberUpdatedState(onQrCodeScanned)

    // Crear una unica vista previa
    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            // Mayor compatibilidad con emuladores
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // Mostrar la camara en Compose
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    // Toda la gestion de la camara: se inicia al entrar y se libera al salir
    DisposableEffect(lifecycleOwner, previewView) {
        val analysisExecutor = Executors.newSingleThreadExecutor()

        // ML Kit limitado solo a QR para optimizar rendimiento
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)

        val yaDetectado = AtomicBoolean(false)
        val liberado = AtomicBoolean(false)
        var cameraProvider: ProcessCameraProvider? = null

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // Descarta fotogramas antiguos para no acumular
        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        analysis.setAnalyzer(analysisExecutor) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage == null || liberado.get()) {
                imageProxy.close()
                return@setAnalyzer
            }

            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    val contenido = barcodes
                        .firstOrNull { !it.rawValue.isNullOrBlank() }
                        ?.rawValue
                    // compareAndSet garantiza que solo se dispare una vez
                    if (contenido != null && yaDetectado.compareAndSet(false, true)) {
                        analysis.clearAnalyzer()
                        Log.d(TAG, "QR detectado")
                        currentCallback(contenido)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error procesando la imagen", e)
                }
                .addOnCompleteListener {
                    // Siempre cerrar el imageProxy para liberar recursos
                    imageProxy.close()
                }
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            if (liberado.get()) return@addListener
            try {
                val provider = cameraProviderFuture.get()
                cameraProvider = provider
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            } catch (e: Exception) {
                Log.e(TAG, "No se pudo iniciar la camara", e)
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            liberado.set(true)
            analysis.clearAnalyzer()
            cameraProvider?.unbindAll()
            scanner.close()
            analysisExecutor.shutdown()
            Log.d(TAG, "Recursos de camara liberados")
        }
    }
}