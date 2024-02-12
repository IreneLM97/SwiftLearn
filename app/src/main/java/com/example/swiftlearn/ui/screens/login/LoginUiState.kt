package com.example.swiftlearn.ui.screens.login

/**
 * Clase de estado que representa el estado actual del inicio de sesión.
 */
data class LoginUiState(
    val loginDetails: LoginDetails = LoginDetails(),
    val remember: Boolean = false,
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
data class LoginDetails(
    val email: String = "",
    val password: String = ""
)
