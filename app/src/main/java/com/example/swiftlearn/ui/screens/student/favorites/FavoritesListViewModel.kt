package com.example.swiftlearn.ui.screens.student.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de anuncios.
 */
class FavoritesListViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de favoritos
    private val _favoritesListUiState = MutableStateFlow(FavoritesListUiState())
    val favoritesListUiState = _favoritesListUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos el usuario autentificado
                val user = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizar el estado de la pantalla con el usuario
                _favoritesListUiState.update { it.copy(user = user) }

                // Combina los flujos de datos de profesores, anuncios y favoritos
                combine(
                    userRepository.getAllProfessors(),
                    advertRepository.getAllAdverts(),
                    favoriteRepository.getAllFavoritesByUser(user._id)
                ) { professors, adverts, favorites  ->
                    val favoritesAdverts = adverts.filter { advert ->
                        favorites.any { it.advertId == advert._id }
                    }
                    Triple(professors, favoritesAdverts, favorites)
                }.collect { (professors, favoritesAdverts, favorites) ->
                    // Actualiza el estado de sesión con los flujos obtenidos
                    _favoritesListUiState.update {
                        it.copy(
                            professorsList = professors,
                            advertsList = favoritesAdverts,
                            favoritesList = favorites,
                            currentAdvert = favoritesAdverts.firstOrNull() ?: Advert()
                        )
                    }

                    delay(1000)
                    _favoritesListUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
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
            val userId = _favoritesListUiState.value.user._id
            favoriteRepository.getFavoriteByInfo(userId = userId, advertId = advert._id)?.let { favorite ->
                favoriteRepository.deleteFavorite(favorite)
            } ?: favoriteRepository.insertFavorite(Favorite(userId = userId, advertId = advert._id))
        }
    }

    fun insertRequest(request: Request) {
        viewModelScope.launch {
            requestRepository.insertRequest(request)
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