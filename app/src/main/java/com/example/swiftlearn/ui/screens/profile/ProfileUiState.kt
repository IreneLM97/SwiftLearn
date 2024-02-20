package com.example.swiftlearn.ui.screens.profile

import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de perfil de usuario.
 *
 * @property userLogged Usuario autentificado.
 * @property profileDetails Detalles del perfil del usuario.
 * @property isEntryValid Indica si el formulario es válido.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class ProfileUiState(
    val userLogged: User = User(),
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = false
)

/**
 * Clase que representa los detalles del perfil.
 *
 * @property username Nombre del usuario.
 * @property phone Número de teléfono del usuario.
 * @property address Dirección del usuario.
 * @property postal Código postal del usuario.
 */
data class ProfileDetails(
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = ""
)

/**
 * Función que convierte un objeto User en sus detalles correspondientes.
 *
 * @return Detalles del usuario generados a partir del objeto User.
 */
fun User.toProfileDetails(): ProfileDetails = ProfileDetails(
    username = username,
    phone = phone,
    address = address,
    postal = postal
)

/**
 * Función que actualiza un usuario dados nuevos datos de perfil.
 *
 * @param user Usuario a actualizar.
 * @return Usuario actualizado con los nuevos datos.
 */
fun ProfileDetails.updateUser(user: User): User {
    return user.copy(
        username = username,
        phone = phone,
        address = address,
        postal = postal
    )
}