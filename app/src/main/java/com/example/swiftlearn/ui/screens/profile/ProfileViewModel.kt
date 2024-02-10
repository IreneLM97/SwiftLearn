package com.example.swiftlearn.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de perfil.
 */
class ProfileViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository
): ViewModel() {
    // Estado de la interfaz de perfil
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtener los datos del usuario desde el repositorio
                val user = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
                _profileUiState.update {
                    val profileDetails = user?.toProfileDetails() ?: ProfileDetails()
                    it.copy(
                        user = user ?: User(),
                        profileDetails = profileDetails,
                        isEntryValid = validateForm(profileDetails)
                    )
                }
            } catch (_: Exception) {}
        }
    }

    fun onFieldChanged(profileDetails: ProfileDetails) {
        _profileUiState.update { it.copy(profileDetails = profileDetails, isEntryValid = validateForm(profileDetails)) }
    }

    fun updateUser(user: User) {
        // Actualizar estado de cargando a true
        _profileUiState.update { it.copy(loadingState = true) }

        viewModelScope.launch {
            userRepository.updateUser(user)

            // Actualizar estado de cargando a false
            _profileUiState.update { it.copy(loadingState = false) }
        }
    }

    fun deleteUser(user: User) {
        // Actualizar estado de cargando a true
        _profileUiState.update { it.copy(loadingState = true) }

        viewModelScope.launch {
            // Eliminamos todos los anuncios de ese usuario
            advertRepository.deleteAllAdvertsByProfId(user._id)

            // Eliminamos el usuario de la base de datos
            userRepository.deleteUser(user)
            // Eliminamos el usuario de la autentificación
            auth.currentUser?.delete()

            // Actualizar estado de cargando a false
            _profileUiState.update { it.copy(loadingState = false) }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    /**
     * Función para validar el formulario de perfil.
     *
     * @param profileDetails Datos del usuario recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(profileDetails: ProfileDetails): Boolean {
        return profileDetails.username.trim().isNotEmpty() &&
                ValidationUtils.isPhoneValid(profileDetails.phone) &&
                profileDetails.address.trim().isNotEmpty() &&
                ValidationUtils.isPostalValid(profileDetails.postal)
    }
}