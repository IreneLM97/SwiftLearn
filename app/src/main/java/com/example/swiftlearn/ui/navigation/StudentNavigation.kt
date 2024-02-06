package com.example.swiftlearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swiftlearn.ui.screens.favorite.FavoriteScreen
import com.example.swiftlearn.ui.screens.home.MenuItems
import com.example.swiftlearn.ui.screens.home.tutor.AdvertScreen
import com.example.swiftlearn.ui.screens.home.tutor.ClassesScreen
import com.example.swiftlearn.ui.screens.home.tutor.MapScreen
import com.example.swiftlearn.ui.screens.profile.ProfileScreen

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
            ProfileScreen()
        }
    }
}