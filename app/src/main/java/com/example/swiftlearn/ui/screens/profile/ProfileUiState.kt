package com.example.swiftlearn.ui.screens.profile

import com.example.swiftlearn.model.User

/**
 * Clase de estado que representa el estado actual de la pantalla de perfil.
 */
data class ProfileUiState(
    val user: User = User(),
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false,
    val loadingState: Boolean = false
)

data class ProfileDetails(
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = ""
)

fun User.toProfileDetails(): ProfileDetails = ProfileDetails(
    username = username,
    phone = phone,
    address = address,
    postal = postal
)

fun ProfileDetails.updateUser(user: User): User {
    return user.copy(
        username = username,
        phone = phone,
        address = address,
        postal = postal
    )
}