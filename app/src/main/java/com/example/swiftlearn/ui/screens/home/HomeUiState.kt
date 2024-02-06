package com.example.swiftlearn.ui.screens.home

import com.example.swiftlearn.model.User

/**
 * Clase de estado que representa el estado actual de la pantalla principal.
 * @property User Usuario que se encuentra iniciado sesión en la aplicación.
 */
data class HomeUiState(
    val user: User
)