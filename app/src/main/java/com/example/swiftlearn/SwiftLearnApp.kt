package com.example.swiftlearn

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swiftlearn.ui.screens.login.LoginScreen
import com.example.swiftlearn.ui.screens.login.LoginViewModel

enum class SwiftLearnScreen {
    LoginScreen,
    RegisterScreen
}

@Composable
fun SwiftLearnApp(
    viewModel: LoginViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Configuración del sistema de navegación
    NavHost(
        navController = navController,
        startDestination = SwiftLearnScreen.LoginScreen.name,
        modifier = Modifier.fillMaxSize()
    ) {
        // Estructura de la pantalla que muestra la lista de categorías (pantalla principal)
        composable(route = SwiftLearnScreen.LoginScreen.name) {
            LoginScreen()
        }
    }
}

private fun shareInfo(
    context: Context,
    summary: String
) {
    // Crear un Intent de acción SEND para compartir
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"  // contenido de texto plano
        putExtra(Intent.EXTRA_TEXT, summary)  // agregamos resumen
    }

    // Iniciar una actividad para elegir la aplicación de destino a la que se quiere compartir
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.send_info)
        )
    )
}