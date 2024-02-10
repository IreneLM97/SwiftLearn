package com.example.swiftlearn.ui.screens.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.R
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
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de registro.
 */
class RegisterViewModel(
    val userRepository: UserRepository
) : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    fun onFieldChanged(registerDetails: RegisterDetails) {
        _registerUiState.update { it.copy(registerDetails = registerDetails, isEntryValid = validateForm(registerDetails)) }
    }

    fun createUserWithEmailAndPassword(
        context: Context,
        registerDetails: RegisterDetails,
        navigateToHome: () -> Unit = {}
    ) {
        if(!_registerUiState.value.loadingState) {
            // Actualizar estado de cargando a true
            _registerUiState.update { it.copy(loadingState = true) }

            auth.createUserWithEmailAndPassword(registerDetails.email, registerDetails.password)
                .addOnCompleteListener {task ->
                    // Actualizar estado de cargando a false
                    _registerUiState.update { it.copy(loadingState = false) }

                    if(task.isSuccessful) {
                        insertUser(registerDetails.toUser())
                        navigateToHome()
                    } else {
                        _registerUiState.update { it.copy(errorMessage = context.getString(R.string.error_register_label)) }
                    }
                }
        }
    }

    private fun insertUser(user: User) {
        // Recogemos el Id que se generó al autentificar al usuario
        val authId = auth.currentUser?.uid

        // Asignamos el Id al usuario
        val userWithAuthId = user.copy(authId = authId.toString())

        // Agregamos el usuario a la colección
        viewModelScope.launch {
            userRepository.insertUser(userWithAuthId)
        }
    }

    /**
     * Función para validar el formulario de registro.
     *
     * @param registerDetails Datos del usuario recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(registerDetails: RegisterDetails): Boolean {
        return registerDetails.username.trim().isNotEmpty() &&
                ValidationUtils.isPhoneValid(registerDetails.phone) &&
                registerDetails.address.trim().isNotEmpty() &&
                ValidationUtils.isPostalValid(registerDetails.postal) &&
                ValidationUtils.isEmailValid(registerDetails.email) &&
                ValidationUtils.isPasswordValid(registerDetails.password) &&
                ValidationUtils.isConfirmPasswordValid(registerDetails.password, registerDetails.confirmPassword)
    }
}