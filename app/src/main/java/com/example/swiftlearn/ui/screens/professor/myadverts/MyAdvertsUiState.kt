package com.example.swiftlearn.ui.screens.professor.myadverts

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User

data class MyAdvertsUiState(
    val user: User = User(),
    val myAdvertsList: List<Advert> = emptyList(),
    val isLoading: Boolean = true
)
