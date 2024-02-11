package com.example.swiftlearn.ui.screens.student.adverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.ui.screens.student.SessionUiState

data class AdvertsListUiState(
    val sessionUiState: SessionUiState = SessionUiState(),
    val advertsList: List<Advert> = emptyList(),
    val currentAdvert: Advert = Advert(),
    val searchQuery: String = "",
    val isShowingListPage: Boolean = true,
    val isLoading: Boolean = true
)
