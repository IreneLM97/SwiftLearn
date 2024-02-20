package com.example.swiftlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Role
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [HomeViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla principal.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 */
class HomeViewModel(
    userRepository: UserRepository
): ViewModel() {
    // Estado de la interfaz de la pantalla principal
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos el rol del usuario desde el repositorio
                val role = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString())?.role

                // Actualizamos el estado de la pantalla con el rol obtenido
                _homeUiState.update { it.copy(role = role ?: Role.None) }
            } catch (_: Exception) {}
        }
    }
}