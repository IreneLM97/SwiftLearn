package com.example.swiftlearn.ui.screens.student.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.ui.screens.student.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de anuncios.
 */
class FavoritesListViewModel(
    val sessionViewModel: SessionViewModel,
    val favoriteRepository: FavoriteRepository
): ViewModel() {
    // Estado de la interfaz de favoritos
    private val _favoritesListUiState = MutableStateFlow(FavoritesListUiState())
    val favoritesListUiState = _favoritesListUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Recolecta el estado de la sesión y actualiza el estado de pantalla
                sessionViewModel.sessionUiState.collect { sessionUiState ->
                    // Obtenemos los Id de los anuncios favoritos marcados por el usuario
                    val favoriteIds = sessionUiState.favoritesList.map { it.advertId }

                    // Filtrar los anuncios que están en la lista de favoritos
                    val favoriteAdverts = sessionUiState.advertsList.filter { advert ->
                        favoriteIds.contains(advert._id)
                    }
                    _favoritesListUiState.update {
                        it.copy(sessionUiState = sessionUiState, advertsList = favoriteAdverts)
                    }

                    delay(600)
                    updateLoadingState()
                }
            } catch (_: Exception) {}
        }
    }

    private fun updateLoadingState() {
        // Actualizamos el estado con el primer elemento de la lista de anuncios seleccionado
        _favoritesListUiState.update {
            it.copy(
                currentAdvert = _favoritesListUiState.value.advertsList.firstOrNull() ?: Advert(),
                isLoading = false
            )
        }
    }

    /**
     * Función que se ejecuta cuando cambia la consulta de búsqueda.
     *
     * @param searchQuery Consulta de búsqueda actual.
     */
    fun onQueryChange(searchQuery: String) {
        // Actualizamos el texto de búsqueda
        _favoritesListUiState.update { it.copy(searchQuery = searchQuery) }
    }

    /**
     * Actualiza el anuncio actual en el estado de la interfaz de usuario.
     *
     * @param selectedAdvert lugar seleccionado por el usuario
     */
    fun updateCurrentAdvert(selectedAdvert: Advert) {
        _favoritesListUiState.update { it.copy(currentAdvert = selectedAdvert) }
    }

    /**
     * Actualiza el estado de favorito de un anuncio.
     */
    fun toggleAdvertFavoriteState(advert: Advert) {
        viewModelScope.launch {
            // Si ese anuncio era favorito -> Se elimina el favorito
            // Si ese anuncio no era favorito (get devuelve null) -> Se inserta el favorito
            val userId = _favoritesListUiState.value.sessionUiState.user._id
            favoriteRepository.getFavoriteByInfo(userId = userId, advertId = advert._id)?.let { favorite ->
                favoriteRepository.deleteFavorite(favorite)
            } ?: favoriteRepository.insertFavorite(Favorite(userId = userId, advertId = advert._id))
        }
    }

    /**
     * Navega a la página que muestra la lista de anuncios.
     */
    fun navigateToListAdvertsPage() {
        _favoritesListUiState.update { it.copy(isShowingListPage = true) }
    }

    /**
     * Navega a la página que muestra los detalles de un anuncio específico.
     */
    fun navigateToDetailAdvertPage() {
        _favoritesListUiState.update { it.copy(isShowingListPage = false) }
    }
}