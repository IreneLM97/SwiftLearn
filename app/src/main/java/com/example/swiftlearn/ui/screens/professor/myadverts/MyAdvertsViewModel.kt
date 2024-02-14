package com.example.swiftlearn.ui.screens.professor.myadverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de anuncios.
 */
class MyAdvertsViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository
): ViewModel() {
    // Estado de la interfaz de anuncios
    private val _myAdvertsUiState = MutableStateFlow(MyAdvertsUiState())
    val myAdvertsUiState = _myAdvertsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // Obtenemos el usuario autentificado
                val user = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizar el estado de la pantalla con el usuario
                _myAdvertsUiState.update { it.copy(user = user) }

                // Obtenemos flujo de datos con los anuncios del profesor
                advertRepository.getAllAdvertsByProfIdFlow(user._id).collect { adverts ->
                    _myAdvertsUiState.update { it.copy(myAdvertsList = adverts) }

                    delay(500)
                    _myAdvertsUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    fun deleteAdvert(advert: Advert) {
        // Actualizar estado de cargando a true
        _myAdvertsUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Eliminamos los favoritos de ese anuncio
            favoriteRepository.deleteAllFavoritesByAdvertId(advert._id)

            // Eliminamos el anuncio seleccionado
            advertRepository.deleteAdvert(advert)

            // Actualizar estado de cargando a false
            _myAdvertsUiState.update { it.copy(isLoading = false) }
        }
    }
}