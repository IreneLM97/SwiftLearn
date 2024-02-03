package com.example.swiftlearn.ui.screens.register

enum class Rol {
    Profesor,
    Alumno
}

/**
 * Clase de estado que representa el estado actual del registro.
 *
 */
data class RegisterUiState(
    val rolValue: Rol = Rol.Profesor,
    val usernameValue: String = "",
    val phoneValue: String = "",
    val addressValue: String = "",
    val postalCodeValue: String = "",
    val emailValue: String = "",
    val passwordValue: String = "",
    val confirmPasswordValue: String = ""
)