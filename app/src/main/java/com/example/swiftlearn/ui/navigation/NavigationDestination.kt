package com.example.swiftlearn.ui.navigation

/**
 * [NavigationDestination] es una interfaz que define una destino de navegación.
 *
 * @property titleRes ID del recurso del título.
 * @property route Ruta del destino de navegación.
 */
interface NavigationDestination {
    val titleRes: Int?
    val route: String
}