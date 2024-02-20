package com.example.swiftlearn.ui.screens.student.favorites

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de anuncios favoritos.
 *
 * @property userLogged Usuario autentificado.
 * @property advertsList Lista de anuncios disponibles.
 * @property professorsList Lista de profesores.
 * @property favoritesList Lista de anuncios favoritos del usuario.
 * @property currentAdvert Anuncio actualmente seleccionado.
 * @property searchQuery Consulta de búsqueda actual.
 * @property isShowingListPage Indica si se está mostrando la página de lista de anuncios.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class FavoritesUiState(
    val userLogged: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val favoritesList: List<Favorite> = emptyList(),
    val currentAdvert: Advert = Advert(),
    val searchQuery: String = "",
    val isShowingListPage: Boolean = true,
    val isLoading: Boolean = true
)
