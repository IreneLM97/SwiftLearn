package com.example.swiftlearn.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swiftlearn.ui.screens.home.HomeDestination
import com.example.swiftlearn.ui.screens.home.HomeScreen
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.example.swiftlearn.ui.screens.login.LoginScreen
import com.example.swiftlearn.ui.screens.register.RegisterDestination
import com.example.swiftlearn.ui.screens.register.RegisterScreen
import com.example.swiftlearn.ui.screens.splash.SplashDestination
import com.example.swiftlearn.ui.screens.splash.SplashScreen

/**
 * [SwiftLearnNavigation] maneja la navegación principal de la aplicación SwiftLearn.
 *
 * @param windowSize Clase de tamaño de ventana.
 * @param navController Controlador de navegación.
 * @param modifier Modificador de diseño.
 * @param startDestination Destino de inicio de la navegación.
 */
@Composable
fun SwiftLearnNavigation(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = SplashDestination.route
) {
    // Configuración del sistema de navegación
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Navegación a la pantalla 'Splash'
        composable(route = SplashDestination.route) {
            SplashScreen(navController = navController)
        }

        // Navegación a la pantalla 'Inicio de sesión'
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navigateToRegister = {
                    navController.navigate(RegisterDestination.route)
                }
            )
        }

        // Navegación a la pantalla 'Registro'
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Navegación a la pantalla 'Home'
        composable(route = HomeDestination.route) {
            HomeScreen(
                windowSize = windowSize,
                mainNavController = navController
            )
        }
    }
}
