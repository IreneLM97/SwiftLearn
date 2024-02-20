package com.example.swiftlearn.ui.screens.register

import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de registro.
 *
 * @param registerDetails Detalles del registro.
 * @param isEntryValid Indica si el formulario es válido.
 * @param errorMessage Mensaje de error si no se ha podido registrar.
 * @param isLoading Indica si la pantalla está en estado de carga.
 */
data class RegisterUiState(
    val registerDetails: RegisterDetails = RegisterDetails(),
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

/**
 * Clase que representa los detalles del registro.
 *
 * @param username Nombre de usuario.
 * @param phone Número de teléfono del usuario.
 * @param address Dirección del usuario.
 * @param postal Código postal del usuario.
 * @param email Correo electrónico del usuario.
 * @param password Contraseña del usuario.
 * @param confirmPassword Confirmación de la contraseña.
 * @param role Rol del usuario al registrarse. Por defecto es alumno.
 */
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

/**
 * Función que convierte los detalles del registro a un objeto User.
 *
 * @return Objeto User generado a partir de los detalles del registro.
 */
fun RegisterDetails.toUser(): User = User(
    username = username,
    phone = phone,
    address = address,
    postal = postal,
    email = email,
    password = password,
    role = role
)