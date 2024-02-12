package com.example.swiftlearn.ui.screens.student.adverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.User

data class AdvertsListUiState(
    val user: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val favoritesList: List<Favorite> = emptyList(),
    val currentAdvert: Advert = Advert(),
    val searchQuery: String = "",
    val isShowingListPage: Boolean = true,
    val isLoading: Boolean = true
)
