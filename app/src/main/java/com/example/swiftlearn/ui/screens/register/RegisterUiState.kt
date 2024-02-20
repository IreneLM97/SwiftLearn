package com.example.swiftlearn.ui.screens.register

import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User

/**
 * Estado de la interfaz de usuario para la pantalla de registro.
 *
 * @property registerDetails Detalles del registro.
 * @property isEntryValid Indica si el formulario es válido.
 * @property errorMessage Mensaje de error si no se ha podido registrar.
 * @property isLoading Indica si la pantalla está en estado de carga.
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
 * @property username Nombre de usuario.
 * @property phone Número de teléfono del usuario.
 * @property address Dirección del usuario.
 * @property postal Código postal del usuario.
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 * @property confirmPassword Confirmación de la contraseña.
 * @property role Rol del usuario al registrarse. Por defecto es alumno.
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