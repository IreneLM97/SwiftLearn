package com.example.swiftlearn.ui.screens.login

/**
 * Clase de estado que representa el estado actual del login.
 *
 * @property emailValue Email del usuario.
 * @property passwordValue Contraseña del usuario.
 * @property rememberValue Toggle que indica si quiere ser recordado o no.
 */
data class LoginUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val rememberValue: Boolean = false
)