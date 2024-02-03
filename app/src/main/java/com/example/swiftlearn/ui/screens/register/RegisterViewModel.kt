package com.example.swiftlearn.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de registro.
 */
class RegisterViewModel() : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    /**
     * Enumeración que representa todos los campos del formulario de registro.
     */
    enum class Field {
        ROL,
        USERNAME,
        PHONE,
        ADDRESS,
        POSTALCODE,
        EMAIL,
        PASSWORD,
        CONFIRMPASSWORD
    }

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param field Campo de entrada que ha cambiado.
     * @param value Nuevo valor del campo de entrada.
     */
    fun onFieldChanged(
        field: Field,
        value: String
    ) {
        // Actualizamos el campo correspondiente
        _registerUiState.update {
            when (field) {
                Field.ROL -> it.copy(rolValue = if (value == "Profesor") Rol.Profesor else Rol.Alumno)
                Field.USERNAME -> it.copy(usernameValue = value)
                Field.PHONE -> it.copy(phoneValue = value)
                Field.ADDRESS -> it.copy(addressValue = value)
                Field.POSTALCODE -> it.copy(postalCodeValue = value)
                Field.EMAIL -> it.copy(emailValue = value)
                Field.PASSWORD -> it.copy(passwordValue = value)
                Field.CONFIRMPASSWORD -> it.copy(confirmPasswordValue = value)
            }
        }
    }

    companion object {
        /**
         * Función para validar el formulario de registro.
         *
         * @param registerUiState Estado actual de la interfaz de registro.
         * @return true si el formulario es válido, false en caso contrario.
         */
        fun validateForm(registerUiState: RegisterUiState): Boolean {
            return registerUiState.usernameValue.trim().isNotEmpty() &&
                    registerUiState.phoneValue.trim().isNotEmpty() &&
                    registerUiState.addressValue.trim().isNotEmpty() &&
                    registerUiState.postalCodeValue.trim().isNotEmpty() &&
                    registerUiState.emailValue.trim().isNotEmpty() &&
                    registerUiState.passwordValue.trim().isNotEmpty() &&
                    registerUiState.confirmPasswordValue.trim().isNotEmpty() &&
                    registerUiState.passwordValue.equals(registerUiState.confirmPasswordValue)
        }
    }
}