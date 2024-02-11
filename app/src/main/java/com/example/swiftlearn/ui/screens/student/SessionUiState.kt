package com.example.swiftlearn.ui.screens.student

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.User

/**
 * Representa el estado común de la sesión de la aplicación, esto es, información que sera
 * utilizada por distintas pantallas de la aplicación.
 *
 * @property user El usuario actualmente autenticado en la aplicación.
 * @property professorsList Lista de profesores disponibles en la aplicación.
 * @property advertsList Lista de anuncios disponibles en la aplicación.
 * @property favoritesList Lista de anuncios marcados como favoritos por el usuario.
 */
data class SessionUiState(
    val user: User = User(),
    val professorsList: List<User> = emptyList(),
    val advertsList: List<Advert> = emptyList(),
    val favoritesList: List<Favorite> = emptyList()
)