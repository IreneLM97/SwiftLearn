package com.example.swiftlearn.ui.screens.map

import com.example.swiftlearn.model.User
import com.google.android.gms.maps.model.LatLng

/**
 * Estado de la interfaz de usuario para la pantalla del mapa.
 *
 * @param userLogged Usuario autentificado.
 * @param searchQuery Consulta de búsqueda.
 * @param searchCoordinates Coordenadas de la búsqueda.
 * @param nearbyProfessors Lista de profesores cercanos.
 * @param professorsList Lista de profesores.
 * @param isLoading Indica si la pantalla está en estado de carga.
 */
data class MapUiState(
    val userLogged: User = User(),
    val searchQuery: String = "",
    val searchCoordinates: LatLng? = null,
    val nearbyProfessors: List<User> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val isLoading: Boolean = true
)
