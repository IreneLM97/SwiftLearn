package com.example.swiftlearn.ui.screens.login

/**
 * Estado de la interfaz de usuario para la pantalla de inicio de sesión.
 *
 * @property loginDetails Detalles del inicio de sesión.
 * @property remember Indica si se debe recordar el inicio de sesión.
 * @property isEntryValid Indica si la entrada de inicio de sesión es válida.
 * @property errorMessage Mensaje de error si no se ha podido iniciar sesión.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class LoginUiState(
    val loginDetails: LoginDetails = LoginDetails(),
    val remember: Boolean = false,
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

/**
 * Representa los detalles del inicio de sesión.
 *
 * @property email Correo electrónico.
 * @property password Contraseña.
 */
data class LoginDetails(
    val email: String = "",
    val password: String = ""
)
