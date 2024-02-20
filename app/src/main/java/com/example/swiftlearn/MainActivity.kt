package com.example.swiftlearn

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.swiftlearn.ui.theme.SwiftLearnTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Clase que representa la actividad principal de la aplicación.
 */
class MainActivity : ComponentActivity() {
    /**
     * Método 'onCreate' que se ejecuta al crear la actividad.
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad.
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitamos permiso para el envío de notificaciones
        askNotificationPermission()
        // Creamos token para el envío de notificaciones personalizadas al dispositivo
        newToken()

        setContent {
            SwiftLearnTheme {
                // Contenedor de la aplicación
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Calculamos el tamaño de la ventana
                    val windowSize = calculateWindowSizeClass(this)

                    // Iniciamos la aplicación de SwiftLearn
                    SwiftLearnApp(windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }

    /**
     * Función para solicitar permiso para el envío de notificaciones.
     */
    private fun askNotificationPermission() {
        // Registro para el resultado de la solicitud de permiso
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

        // Verificamos si la versión de Android es igual o superior a TIRAMISU
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificamos si tiene permiso para enviar notificaciones
            if(ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }
    }

    /**
     * Función para registrar un nuevo token completo para el envío de notificaciones.
     */
    private fun newToken() {
        // Obtenemos una nueva instancia de FirebaseMessaging y registramos un token completo
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if(!task.isSuccessful) {
                    // Si la tarea no fue exitosa salimos del Listener
                    return@OnCompleteListener
                }
                // Obtenemos el token resultado de la tarea
                val token = task.result
            }
        )
    }
}