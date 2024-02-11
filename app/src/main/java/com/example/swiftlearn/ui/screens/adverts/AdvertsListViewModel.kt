package com.example.swiftlearn.ui.screens.adverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de anuncios.
 */
class AdvertsListViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository
): ViewModel() {
    // Estado de la interfaz de anuncios
    private val _advertsListUiState = MutableStateFlow(AdvertsListUiState())
    val advertsListUiState = _advertsListUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos flujo de datos de los profesores
                userRepository.getAllProfessors().collect { users ->
                    // Actualizar el estado de la pantalla con los profesores obtenidos
                    _advertsListUiState.update { it.copy(professorsList = users) }
                    updateLoadingState()
                }
            } catch (_: Exception) {}
        }

        viewModelScope.launch {
            try {
                // Obtenemos flujo de datos de los anuncios
                advertRepository.getAllAdverts().collect { adverts ->
                    // Actualizar el estado de la pantalla con los anuncios obtenidos
                    _advertsListUiState.update { it.copy(advertsList = adverts, currentAdvert = adverts[0]) }
                    updateLoadingState()
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