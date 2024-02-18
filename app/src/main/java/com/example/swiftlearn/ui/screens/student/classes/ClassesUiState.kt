package com.example.swiftlearn.ui.screens.student.classes

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User

data class ClassesUiState(
    val userLogged: User = User(),
    val advertsList: List<Advert> = emptyList(),
    val professorsList: List<User> = emptyList(),
    val pendingRequests: List<Request> = emptyList(),
    val acceptedRequests: List<Request> = emptyList(),
    val deniedRequests: List<Request> = emptyList(),
    val isLoading: Boolean = true
)