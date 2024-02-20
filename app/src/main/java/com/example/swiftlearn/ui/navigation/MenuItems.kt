package com.example.swiftlearn.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.swiftlearn.R

/**
 * Enumeración de los elementos del menú de navegación.
 *
 * @property outlinedIconRes ID del recurso del icono de contorno.
 * @property filledIconRes ID del recurso del icono relleno.
 * @property titleRes ID del recurso del título.
 * @property route Ruta del elemento del menú.
 */
sealed class MenuItems(
    @DrawableRes val outlinedIconRes: Int,
    @DrawableRes val filledIconRes: Int,
    @StringRes val titleRes: Int,
    val route: String
) {
    // Elemento del menú 'Anuncios'
    object AdvertsItem: MenuItems(R.drawable.outlined_icon_advert, R.drawable.filled_icon_advert, R.string.title_adverts, "adverts")

    // Elemento del menú 'Clases'
    object ClassesItem: MenuItems(R.drawable.outlined_icon_classes, R.drawable.filled_icon_classes, R.string.title_classes, "classes")

    // Elemento del menú 'Mapa'
    object MapItem: MenuItems(R.drawable.outlined_icon_map, R.drawable.filled_icon_map, R.string.title_map, "map")

    // Elemento del menú 'Perfil'
    object ProfileItem: MenuItems(R.drawable.outlined_icon_user, R.drawable.filled_icon_user, R.string.title_profile, "profile")

    // Elemento del menú 'Nuevo anuncio'
    object NewAdvertItem: MenuItems(R.drawable.icon_add, R.drawable.icon_add, R.string.title_new_advert, "new_advert")

    // Elemento del menú 'Favoritos'
    object FavoritesItem: MenuItems(R.drawable.outlined_icon_favorite, R.drawable.filled_icon_favorite, R.string.title_favorite, "favorites")
}

// Lista de elementos del menú para profesores
val professorMenuItems = listOf(
    MenuItems.AdvertsItem,
    MenuItems.ClassesItem,
    MenuItems.MapItem,
    MenuItems.ProfileItem
)

// Lista de elementos del menú para alumnos
val studentMenuItems = listOf(
    MenuItems.AdvertsItem,
    MenuItems.ClassesItem,
    MenuItems.FavoritesItem,
    MenuItems.MapItem,
    MenuItems.ProfileItem
)
