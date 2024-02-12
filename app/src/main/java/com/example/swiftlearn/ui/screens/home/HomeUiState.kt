package com.example.swiftlearn.ui.screens.home

import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User

/**
 * Clase de estado que representa el estado actual de la pantalla principal.
 * @property role Role del usuario que se encuentra autentificado.
 * @property route Ruta de navegación.
 */
data class HomeUiState(
    val role: Role = Role.None,
    val route: String = ""
)