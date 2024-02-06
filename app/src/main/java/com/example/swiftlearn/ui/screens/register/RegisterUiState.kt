package com.example.swiftlearn.ui.screens.register

import com.example.swiftlearn.model.Rol

/**
 * Clase de estado que representa el estado actual del registro.
 *
 */
data class RegisterUiState(
    val usernameValue: String = "",
    val phoneValue: String = "",
    val addressValue: String = "",
    val postalValue: String = "",
    val emailValue: String = "",
    val passwordValue: String = "",
    val confirmPasswordValue: String = "",
    val rolValue: Rol = Rol.Profesor
)