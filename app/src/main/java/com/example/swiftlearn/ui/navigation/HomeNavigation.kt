@file:Suppress("UNUSED_EXPRESSION")

package com.example.swiftlearn.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Role
import com.example.swiftlearn.ui.screens.student.adverts.AdvertsScreen
import com.example.swiftlearn.ui.screens.home.HomeDestination
import com.example.swiftlearn.ui.screens.home.HomeUiState
import com.example.swiftlearn.ui.screens.map.MapScreen
import com.example.swiftlearn.ui.screens.professor.newadvert.NewAdvertScreen
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.example.swiftlearn.ui.screens.professor.myclasses.MyClassesScreen
import com.example.swiftlearn.ui.screens.professor.editadvert.EditAdvertDestination
import com.example.swiftlearn.ui.screens.professor.editadvert.EditAdvertScreen
import com.example.swiftlearn.ui.screens.professor.myadverts.MyAdvertsScreen
import com.example.swiftlearn.ui.screens.profile.ProfileScreen
import com.example.swiftlearn.ui.screens.student.classes.ClassesScreen
import com.example.swiftlearn.ui.screens.student.favorites.FavoritesScreen

@Composable
fun HomeNavigation(
    homeUiState: HomeUiState,
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    mainNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Contexto de la aplicación
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = MenuItems.AdvertsItem.route,
        modifier = modifier
    ) {
        composable(route = MenuItems.AdvertsItem.route) {
            when(homeUiState.role) {
                Role.Profesor -> {
                    MyAdvertsScreen(
                        navigateToEditAdvert = { navController.navigate("${EditAdvertDestination.route}/$it") },
                        onSendButtonClick = { sendAdvert(context, it) }
                    )
                }
                Role.Alumno -> {
                    AdvertsScreen(
                        windowSize = windowSize,
                        onSendButtonClick = { sendAdvert(context, it) },
                        navigateToClasses = { navController.navigate(MenuItems.ClassesItem.route) }
                    )
                }
                Role.None -> null
            }

        }
        composable(route = MenuItems.ClassesItem.route) {
            when(homeUiState.role) {
                Role.Profesor -> MyClassesScreen()
                Role.Alumno -> ClassesScreen()
                Role.None -> null
            }
        }
        composable(route = MenuItems.FavoritesItem.route) {
            FavoritesScreen(
                windowSize = windowSize,
                onSendButtonClick = { sendAdvert(context, it) },
                navigateToClasses = { navController.navigate(MenuItems.ClassesItem.route) }
            )
        }
        composable(route = MenuItems.MapItem.route) {
            MapScreen()
        }
        composable(route = MenuItems.ProfileItem.route) {
            ProfileScreen(
                navigateToLogin = { navigateToLogin(mainNavController) }
            )
        }
        composable(route = MenuItems.NewAdvertItem.route) {
            NewAdvertScreen(
                navigateToListAdverts = { navController.navigate(MenuItems.AdvertsItem.route) }
            )
        }
        composable(
            route = EditAdvertDestination.routeWithArgs,
            arguments = listOf(navArgument(EditAdvertDestination.advertIdArg) { type = NavType.StringType })
        ) {
            EditAdvertScreen(
                navigateToListAdverts = { navController.navigate(MenuItems.AdvertsItem.route) }
            )
        }
    }
}

fun navigateToLogin(mainNavController: NavHostController) {
    mainNavController.navigate(LoginDestination.route) {
        popUpTo(HomeDestination.route) {
            inclusive = true
        }
    }
}

/**
 * Función que permite compartir la información de un anuncio a otra aplicación.
 *
 * @param context Contexto de la aplicación.
 * @param summary Resumen del anuncio que se quiere compartir.
 */
private fun sendAdvert(
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
            context.getString(R.string.advert_summary)
        )
    )
}