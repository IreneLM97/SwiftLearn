package com.example.swiftlearn.ui.screens.map

import com.example.swiftlearn.model.User
import com.google.android.gms.maps.model.LatLng

/**
 * Estado de la interfaz de usuario para la pantalla del mapa.
 *
 * @property userLogged Usuario autentificado.
 * @property searchQuery Consulta de búsqueda.
 * @property searchCoordinates Coordenadas de la búsqueda.
 * @property nearbyProfessors Lista de profesores cercanos.
 * @property professorsList Lista de profesores.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class MapUiState(
    val userLogged: User = User(),
    val searchQuery: String = "",
    val searchCoordinates: LatLng? = null,
    val nearbyProfessors: List<User> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val isLoading: Boolean = true
)
