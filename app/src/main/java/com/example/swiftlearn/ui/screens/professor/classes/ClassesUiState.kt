package com.example.swiftlearn.ui.screens.professor.classes

import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User

data class ClassesUiState(
    val user: User = User(),
    val requestsList: List<Request> = emptyList(),
    val isLoading: Boolean = true
)