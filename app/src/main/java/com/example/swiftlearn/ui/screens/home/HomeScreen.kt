@file:Suppress("UNUSED_EXPRESSION")

package com.example.swiftlearn.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Rol
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.FloatingButtonNavigation
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.components.NavInfProfessor
import com.example.swiftlearn.ui.components.NavInfStudent
import com.example.swiftlearn.ui.navigation.HomeNavigation
import com.example.swiftlearn.ui.screens.login.LoginDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.login_title
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla principal
    val homeUiState = viewModel.homeUiState.collectAsState().value

    // Recordar el controlador de navegación
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            when (homeUiState.user.rol) {
                Rol.Profesor -> NavInfProfessor(
                    navController = navController,
                    menuItems = professorMenuItems
                )
                Rol.Alumno -> NavInfStudent(
                    navController = navController,
                    menuItems = studentMenuItems
                )
                Rol.None -> null
            }
        },
        floatingActionButton = {
            if (homeUiState.user.rol == Rol.Profesor) FloatingButtonNavigation(navController = navController)
        },
        isFloatingActionButtonDocked = homeUiState.user.rol == Rol.Profesor
    ) {
        HomeNavigation(navController = navController, mainNavController = mainNavController)
    }
}
