package com.example.swiftlearn.ui.screens.professor.myclasses

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User

data class MyClassesUiState(
    val user: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val studentsList: List<User> = emptyList(),
    val pendingRequests: List<Request> = emptyList(),
    val acceptedRequests: List<Request> = emptyList(),
    val deniedRequests: List<Request> = emptyList(),
    val isLoading: Boolean = true
)