package com.example.swiftlearn.ui.screens.student.classes

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de solicitudes de clases.
 *
 * @property userLogged Usuario autentificado.
 * @property advertsList Lista de anuncios disponibles.
 * @property professorsList Lista de profesores.
 * @property pendingRequests Lista de solicitudes pendientes.
 * @property acceptedRequests Lista de solicitudes aceptadas.
 * @property deniedRequests Lista de solicitudes rechazadas.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class ClassesUiState(
    val userLogged: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val pendingRequests: List<Request> = emptyList(),
    val acceptedRequests: List<Request> = emptyList(),
    val deniedRequests: List<Request> = emptyList(),
    val isLoading: Boolean = true
)