package com.example.swiftlearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swiftlearn.ui.screens.favorite.FavoriteScreen
import com.example.swiftlearn.ui.screens.home.HomeDestination
import com.example.swiftlearn.ui.screens.home.MenuItems
import com.example.swiftlearn.ui.screens.home.professor.AdvertScreen
import com.example.swiftlearn.ui.screens.home.professor.ClassesScreen
import com.example.swiftlearn.ui.screens.home.professor.MapScreen
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.example.swiftlearn.ui.screens.login.LoginScreen
import com.example.swiftlearn.ui.screens.profile.ProfileScreen
import com.example.swiftlearn.ui.screens.register.RegisterDestination

@Composable
fun StudentNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MenuItems.AdvertsItem.route,
        modifier = modifier
    ) {
        composable(route = MenuItems.AdvertsItem.route) {
            AdvertScreen()
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
                navigateToLogin = { navController.navigate(LoginDestination.route) }
            )
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
    }
}