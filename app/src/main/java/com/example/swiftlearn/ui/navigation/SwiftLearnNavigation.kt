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
import com.example.swiftlearn.ui.screens.splash.SplashScreen

enum class SwiftLearnScreens {
    SplashScreen
}

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun SwiftLearnNavigation(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = SwiftLearnScreens.SplashScreen.name
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = SwiftLearnScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
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
        composable(route = HomeDestination.route) {
            HomeScreen(
                windowSize = windowSize,
                mainNavController = navController
            )
        }
    }
}
