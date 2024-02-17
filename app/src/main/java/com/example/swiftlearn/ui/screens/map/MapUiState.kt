package com.example.swiftlearn.ui.screens.map

import com.example.swiftlearn.model.User

data class MapUiState(
    val user: User = User(),
    val searchQuery: String = "",
    val searchCoordinates: Pair<Double, Double>? = null,
    val isLoading: Boolean = true
)
