package com.example.swiftlearn.ui.screens.register

import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User

/**
 * Clase de estado que representa el estado actual del registro.
 */
data class RegisterUiState(
    val registerDetails: RegisterDetails = RegisterDetails(),
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null,
    val loadingState: Boolean = false
)

data class RegisterDetails(
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: Role = Role.Alumno
)

fun RegisterDetails.toUser(): User = User(
    username = username,
    phone = phone,
    address = address,
    postal = postal,
    email = email,
    password = password,
    role = role
)