package com.example.swiftlearn.ui.screens.adverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.profile.ProfileDetails
import com.example.swiftlearn.ui.screens.profile.toProfileDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de anuncios.
 */
class AdvertsListViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository
): ViewModel() {
    // Estado de la interfaz de anuncios
    private val _advertsListUiState = MutableStateFlow(AdvertsListUiState())
    val advertsListUiState = _advertsListUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        // Obtener el usuario autentificado
        viewModelScope.launch {
            val user = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())
            // Actualizar el estado de la pantalla
            _advertsListUiState.update {it.copy(user = user ?: User()) }
        }

        // Obtenemos flujo de datos de los anuncios
        viewModelScope.launch {
            try {
                advertRepository.getAllAdverts().collect { adverts ->
                    // Actualizar el estado de la pantalla con los anuncios obtenidos
                    _advertsListUiState.update { it.copy(advertsList = adverts, currentAdvert = adverts[0]) }
                    // Actualizamos estado de cargando
                    updateLoadingState()
                }
            } catch (_: Exception) {}
        }

        // Obtenemos flujo de datos de los profesores
        viewModelScope.launch {
            try {
                userRepository.getAllProfessors().collect { users ->
                    // Actualizar el estado de la pantalla con los profesores obtenidos
                    _advertsListUiState.update { it.copy(professorsList = users) }
                    // Actualizamos estado de cargando
                    updateLoadingState()
                }
            } catch (_: Exception) {}
        }

        // Obtenemos flujo de datos de los favoritos
        viewModelScope.launch {
            try {
                favoriteRepository.getAllFavoritesByUser(auth.currentUser?.uid.toString()).collect { favorites ->
                    // Actualizar el estado de la pantalla con los favoritos obtenidos
                    _advertsListUiState.update { it.copy(favoritesList = favorites) }
                }
            } catch (_: Exception) {}
        }
    }

    private fun updateLoadingState() {
        if(_advertsListUiState.value.advertsList.isNotEmpty() && _advertsListUiState.value.professorsList.isNotEmpty()) {
            _advertsListUiState.update { it.copy(loadingState = false) }
        }
    }

    /**
     * Función que se ejecuta cuando cambia la consulta de búsqueda.
     *
     * @param searchQuery Consulta de búsqueda actual.
     */
    fun onQueryChange(searchQuery: String) {
        // Actualizamos el texto de búsqueda
        _advertsListUiState.update { it.copy(searchQuery = searchQuery) }
    }

    /**
     * Actualiza el anuncio actual en el estado de la interfaz de usuario.
     *
     * @param selectedAdvert lugar seleccionado por el usuario
     */
    fun updateCurrentPlace(selectedAdvert: Advert) {
        _advertsListUiState.update { it.copy(currentAdvert = selectedAdvert) }
    }

    /**
     * Actualiza el estado de favorito de un anuncio.
     */
    fun toggleAdvertFavoriteState(advert: Advert) {
        viewModelScope.launch {
            // Si ese anuncio era favorito -> Se elimina el favorito
            // Si ese anuncio no era favorito (get devuelve null) -> Se inserta el favorito
            favoriteRepository.getFavoriteByInfo(userId = advertsListUiState.value.user._id, advertId = advert._id)?.let { favorite ->
                favoriteRepository.deleteFavorite(favorite)
            } ?: favoriteRepository.insertFavorite(Favorite(userId = advertsListUiState.value.user._id, advertId = advert._id))
        }
    }

    /**
     * Navega a la página que muestra la lista de anuncios.
     */
    fun navigateToListAdvertsPage() {
        _advertsListUiState.update { it.copy(isShowingListPage = true) }
    }

    /**
     * Navega a la página que muestra los detalles de un anuncio específico.
     */
    fun navigateToDetailAdvertPage() {
        _advertsListUiState.update { it.copy(isShowingListPage = false) }
    }
}