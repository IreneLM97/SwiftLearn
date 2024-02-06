package com.example.swiftlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla principal.
 */
class HomeViewModel(
    userRepository: UserRepository
): ViewModel() {
    // Estado de la interfaz de pantalla principal
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtener los datos del usuario desde el repositorio
                val user = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
                _homeUiState.value = _homeUiState.value.copy(user = user ?: User())
            } catch (_: Exception) {}
        }
    }
}