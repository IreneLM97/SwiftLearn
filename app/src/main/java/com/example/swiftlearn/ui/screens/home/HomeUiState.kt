package com.example.swiftlearn.ui.screens.home

import com.example.swiftlearn.model.Role

/**
 * Estado de la interfaz de usuario para la pantalla principal.
 *
 * @property role Rol del usuario.
 */
data class HomeUiState(
    val role: Role = Role.None
)