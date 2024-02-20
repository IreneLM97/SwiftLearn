package com.example.swiftlearn.ui.screens.professor.myclasses

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de mis clases.
 *
 * @param userLogged Usuario autentificado.
 * @param advertsList Lista de anuncios del usuario.
 * @param studentsList Lista de estudiantes.
 * @param pendingRequests Lista de solicitudes pendientes.
 * @param acceptedRequests Lista de solicitudes aceptadas.
 * @param deniedRequests Lista de solicitudes rechazadas.
 * @param isLoading Indica si la pantalla está en estado de carga.
 */
data class MyClassesUiState(
    val userLogged: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val studentsList: List<User> = emptyList(),
    val pendingRequests: List<Request> = emptyList(),
    val acceptedRequests: List<Request> = emptyList(),
    val deniedRequests: List<Request> = emptyList(),
    val isLoading: Boolean = true
)