@file:Suppress("UNUSED_EXPRESSION")

package com.example.swiftlearn.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Role
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.FloatingButtonNavigation
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.components.NavInfProfessor
import com.example.swiftlearn.ui.components.NavInfStudent
import com.example.swiftlearn.ui.navigation.HomeNavigation

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.login_title
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    windowSize: WindowWidthSizeClass,
    mainNavController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla principal
    val homeUiState = viewModel.homeUiState.collectAsState().value

    // Recordar el controlador de navegación
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            when (homeUiState.user.role) {
                Role.Profesor -> NavInfProfessor(
                    navController = navController,
                    menuItems = professorMenuItems
                )
                Role.Alumno -> NavInfStudent(
                    navController = navController,
                    menuItems = studentMenuItems
                )
                Role.None -> null
            }
        },
        floatingActionButton = {
            if (homeUiState.user.role == Role.Profesor) FloatingButtonNavigation(navController = navController)
        },
        isFloatingActionButtonDocked = homeUiState.user.role == Role.Profesor
    ) {
        HomeNavigation(windowSize = windowSize, navController = navController, mainNavController = mainNavController)
    }
}
