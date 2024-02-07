package com.example.swiftlearn.ui.screens.adverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
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
class AdvertViewModel(
    val userRepository: UserRepository
): ViewModel() {
    // Estado de la interfaz de anuncios
    private val _advertUiState = MutableStateFlow(AdvertUiState())
    val advertUiState = _advertUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Actualizar el valor de authId según la autentificación
                _advertUiState.update {
                    it.copy(
                        user = userRepository.getUserByAuthId(auth.currentUser?.uid.toString()) ?: User()
                    )
                }
            } catch (_: Exception) {}
        }
    }
}