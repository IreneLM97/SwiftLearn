package com.example.swiftlearn.ui.screens.map

import com.example.swiftlearn.model.User
import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val user: User = User(),
    val searchQuery: String = "",
    val searchCoordinates: LatLng? = null,
    val nearbyProfCoordinates: List<LatLng> = emptyList(),
    val nearbyProfList: List<User> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val isLoading: Boolean = true
)
