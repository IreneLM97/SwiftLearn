package com.example.swiftlearn.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.screens.adverts.AdvertsListScreen
import com.example.swiftlearn.ui.screens.favorites.FavoriteScreen
import com.example.swiftlearn.ui.screens.home.HomeDestination
import com.example.swiftlearn.ui.screens.home.MenuItems
import com.example.swiftlearn.ui.screens.home.professor.ClassesScreen
import com.example.swiftlearn.ui.screens.home.professor.MapScreen
import com.example.swiftlearn.ui.screens.newadvert.NewAdvertScreen
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.example.swiftlearn.ui.screens.profile.ProfileScreen

@Composable
fun HomeNavigation(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    mainNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = MenuItems.AdvertsItem.route,
        modifier = modifier
    ) {
        composable(route = MenuItems.AdvertsItem.route) {
            AdvertsListScreen(
                windowSize = windowSize,
                onSendButtonClick = { shareAdvert(context, it) }
            )
        }
        composable(route = MenuItems.ClassesItem.route) {
            ClassesScreen()
        }
        composable(route = MenuItems.FavoritesItem.route) {
            FavoriteScreen()
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
private fun shareAdvert(
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