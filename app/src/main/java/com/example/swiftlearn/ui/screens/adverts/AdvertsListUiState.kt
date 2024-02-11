package com.example.swiftlearn.ui.screens.adverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.User
import kotlinx.coroutines.flow.Flow

data class AdvertsListUiState(
    val searchQuery: String = "",
    val user: User = User(),
    val currentAdvert: Advert = Advert(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val favoritesList: List<Favorite> = emptyList(),
    val isShowingListPage: Boolean = true,
    val loadingState: Boolean = true
)
