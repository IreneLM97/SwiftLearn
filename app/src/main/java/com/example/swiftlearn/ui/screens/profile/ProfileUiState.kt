package com.example.swiftlearn.ui.screens.profile

import com.example.swiftlearn.model.Rol
import com.example.swiftlearn.model.User

/**
 * Clase de estado que representa el estado actual de la pantalla de perfil.
 */
data class ProfileUiState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false,
    val loadingState: Boolean = false
)

data class ProfileDetails(
    val authId: String = "",
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = "",
    val email: String = "",
    val rol: Rol = Rol.Profesor
)

fun ProfileDetails.toUser(): User = User(
    authId = authId,
    username = username,
    phone = phone,
    address = address,
    postal = postal,
    email = email,
    rol = rol
)

fun User.toProfileDetails(): ProfileDetails = ProfileDetails(
    authId = authId,
    username = username,
    phone = phone,
    address = address,
    postal = postal,
    email = email,
     rol = rol
)