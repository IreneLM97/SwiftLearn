package com.example.swiftlearn.ui.screens.adverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User
import kotlinx.coroutines.flow.Flow

data class AdvertsListUiState(
    val searchQuery: String = "",
    val currentAdvert: Advert = Advert(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val isShowingListPage: Boolean = true
)
