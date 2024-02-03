package com.example.swiftlearn.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Field {
    EMAIL,
    PASSWORD
}

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
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param field Campo de entrada que ha cambiado.
     * @param value Nuevo valor del campo de entrada.
     */
    fun onFieldChanged(
        field: Field, value: String
    ) {
        // Actualizamos el campo correspondiente
        _loginUiState.update {
            when (field) {
                Field.EMAIL -> it.copy(emailValue = value)
                Field.PASSWORD -> it.copy(passwordValue = value)
            }
        }

        // Si está marcado el toggle de recordar y se cambió el email, guardamos en preferencias el email
        if (loginUiState.value.rememberValue && field == Field.EMAIL) {
            viewModelScope.launch {
                userPreferencesRepository.saveUserPreferences(
                    emailValue = loginUiState.value.emailValue,
                    rememberValue = true
                )
            }
        }
    }

    /**
     * Función que se ejecuta cuando cambia el toggle.
     *
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