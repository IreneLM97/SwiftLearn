package com.example.swiftlearn.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.swiftlearn.R

sealed class MenuItems(
    @DrawableRes val outlinedIconRes: Int,
    @DrawableRes val filledIconRes: Int,
    @StringRes val titleRes: Int,
    val route: String
) {
    object AdvertsItem: MenuItems(R.drawable.outlined_icon_advert, R.drawable.filled_icon_advert, R.string.title_adverts, "adverts")
    object ClassesItem: MenuItems(R.drawable.outlined_icon_classes, R.drawable.filled_icon_classes, R.string.title_requests, "requests")
    object MapItem: MenuItems(R.drawable.outlined_icon_map, R.drawable.filled_icon_map, R.string.title_map, "map")
    object ProfileItem: MenuItems(R.drawable.outlined_icon_user, R.drawable.filled_icon_user, R.string.title_profile, "profile")
    object NewAdvertItem: MenuItems(R.drawable.icon_add, R.drawable.icon_add, R.string.title_new_advert, "new_advert")
    object FavoritesItem: MenuItems(R.drawable.outlined_icon_favorite, R.drawable.filled_icon_favorite, R.string.title_favorite, "favorites")
}

// Lista de ítems del menú para profesores
val professorMenuItems = listOf(
    MenuItems.AdvertsItem,
    MenuItems.ClassesItem,
    MenuItems.MapItem,
    MenuItems.ProfileItem
)

// Lista de ítems del menú para alumnos
val studentMenuItems = listOf(
    MenuItems.AdvertsItem,
    MenuItems.ClassesItem,
    MenuItems.FavoritesItem,
    MenuItems.MapItem,
    MenuItems.ProfileItem
)
