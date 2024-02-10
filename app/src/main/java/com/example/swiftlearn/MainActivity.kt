package com.example.swiftlearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.example.swiftlearn.ui.theme.SwiftLearnTheme

class MainActivity : ComponentActivity() {
    /**
     * Método `onCreate` que se ejecuta al crear la actividad.
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwiftLearnTheme {
                // A surface container using the 'background' color from the theme
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
}