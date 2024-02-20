package com.example.swiftlearn.ui.screens.professor.myadverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de mis anuncios.
 *
 * @property userLogged Usuario autentificado.
 * @property myAdvertsList Lista de anuncios del usuario.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class MyAdvertsUiState(
    val userLogged: User = User(),
    val myAdvertsList: List<Advert> = emptyList(),
    val isLoading: Boolean = true
)
