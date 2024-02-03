package com.example.swiftlearn.ui.screens.register

import androidx.lifecycle.ViewModel
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import com.example.swiftlearn.ui.screens.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(

) : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()
}