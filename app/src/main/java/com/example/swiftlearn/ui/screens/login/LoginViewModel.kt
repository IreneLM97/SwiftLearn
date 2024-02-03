package com.example.swiftlearn.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de búsqueda.
 *
 * @param userPreferencesRepository Repositorio para acceder a las preferencias del usuario.
 */
class LoginViewModel(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Estado de la interfaz de inicio de sesión
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            // Observamos los cambios en las preferencias del usuario
            userPreferencesRepository.userPreferences.collect {userPreferences ->
                if(userPreferences.rememberValue) {
                    _loginUiState.update {
                        it.copy(
                            emailValue = userPreferences.emailValue,
                            rememberValue = true
                        )
                    }
                }
            }
        }
    }

    /**
     * Función que se ejecuta cuando cambia el email.
     *
     * @param emailValue Valor del correo del usuario.
     */
    fun onEmailChanged(
        emailValue: String
    ) {
        // Actualizamos el email
        _loginUiState.update { it.copy(emailValue = emailValue) }

        // Si está marcado el toggle de recordar, guardamos en preferencias el email
        if(loginUiState.value.rememberValue) {
            viewModelScope.launch {
                userPreferencesRepository.saveUserPreferences(
                    emailValue = loginUiState.value.emailValue,
                    rememberValue = true
                )
            }
        }
    }

    /**
     * Función que se ejecuta cuando cambia la contraseña.
     *
     * @param passwordValue Valor de la contraseña del usuario.
     */
    fun onPasswordChanged(
        passwordValue: String
    ) {
        // Actualizamos la contraseña
        _loginUiState.update { it.copy(passwordValue = passwordValue) }
    }

    /**
     * Función que se ejecuta cuando cambia el toggle.
     *
     * @param emailValue Valor del correo del usuario.
     * @param rememberValue Valor del toggle que indica si quiere ser recordado o no.
     */
    fun onToggleChanged(
        rememberValue: Boolean = false
    ) {
        // Actualizamos el estado de la página
        _loginUiState.update {it.copy(rememberValue = rememberValue) }

        // Actualizamos las preferencias del usuario
        viewModelScope.launch {
            userPreferencesRepository.saveUserPreferences(
                emailValue = if(rememberValue) loginUiState.value.emailValue else "",
                rememberValue = rememberValue
            )
        }
    }
}