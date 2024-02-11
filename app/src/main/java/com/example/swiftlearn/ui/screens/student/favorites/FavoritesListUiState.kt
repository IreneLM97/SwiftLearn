package com.example.swiftlearn.ui.screens.student.favorites

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.ui.screens.student.SessionUiState

data class FavoritesListUiState(
    val sessionUiState: SessionUiState = SessionUiState(),
    val advertsList: List<Advert> = emptyList(),
    val currentAdvert: Advert = Advert(),
    val searchQuery: String = "",
    val isShowingListPage: Boolean = true,
    val isLoading: Boolean = true
)
