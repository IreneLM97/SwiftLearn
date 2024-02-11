package com.example.swiftlearn.ui.screens.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository

): ViewModel() {
    private val _sessionUiState = MutableStateFlow(SessionUiState())
    val sessionUiState: StateFlow<SessionUiState> = _sessionUiState

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos el usuario autentificado
                val user = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizar el estado de la pantalla con el usuario
                _sessionUiState.update { it.copy(user = user) }

                // Combina los flujos de datos de profesores, anuncios y favoritos
                combine(
                    userRepository.getAllProfessors(),
                    advertRepository.getAllAdverts(),
                    favoriteRepository.getAllFavoritesByUser(user._id)
                ) { professors, adverts, favorites  ->
                    Triple(professors, adverts, favorites)
                }.collect { (professors, adverts, favorites) ->
                    // Actualiza el estado de sesión con los flujos obtenidos
                    _sessionUiState.update { it.copy(professorsList = professors, advertsList = adverts, favoritesList = favorites) }
                }
            } catch (_: Exception) {}
        }
    }
}