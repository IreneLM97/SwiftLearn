package com.example.swiftlearn.ui.screens.professor.myadverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepository
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
 * [MyAdvertsViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de mis anuncios.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 * @param favoriteRepository Repositorio para gestionar la colección favoritos.
 * @param requestRepository Repositorio para gestionar la colección solicitudes.
 */
class MyAdvertsViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de mis anuncios
    private val _myAdvertsUiState = MutableStateFlow(MyAdvertsUiState())
    val myAdvertsUiState = _myAdvertsUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
                _myAdvertsUiState.update { it.copy(userLogged = userLogged) }

                // Obtenemos flujo de datos con los anuncios del profesor
                advertRepository.getAllAdvertsByProfIdFlow(userLogged._id).collect { adverts ->
                    _myAdvertsUiState.update { it.copy(myAdvertsList = adverts) }

                    delay(500)
                    // Actualizamos estado de cargando a false
                    _myAdvertsUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función para eliminar un anuncio y todas sus referencias asociadas.
     *
     * @param advert Anuncio a eliminar.
     */
    fun deleteAdvert(advert: Advert) {
        // Actualizar estado de cargando a true
        _myAdvertsUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Eliminamos los favoritos asociados a ese anuncio
            favoriteRepository.deleteAllFavoritesByAdvertId(advert._id)

            // Eliminamos las clases asociadas a ese anuncio
            requestRepository.deleteAllRequestsByAdvertId(advert._id)

            // Eliminamos el anuncio seleccionado
            advertRepository.deleteAdvert(advert)

            // Actualizar estado de cargando a false
            _myAdvertsUiState.update { it.copy(isLoading = false) }
        }
    }
}