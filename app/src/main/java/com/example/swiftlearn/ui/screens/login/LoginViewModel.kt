package com.example.swiftlearn.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.R
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import com.example.swiftlearn.ui.screens.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de inicio de sesión.
 *
 * @param userPreferencesRepository Repositorio para acceder a las preferencias del usuario.
 */
class LoginViewModel(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Estado de la interfaz de inicio de sesión
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Estado para almacenar el mensaje de error si no se puede iniciar sesión
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado para indicar si se está realizando el proceso de inicio de sesión
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            // Observamos los cambios en las preferencias del usuario
            userPreferencesRepository.userPreferences.collect {userPreferences ->
                if(userPreferences.rememberValue) {
                    _loginUiState.update {
                        it.copy(
                            emailValue = userPreferences.emailValue,
                            passwordValue = userPreferences.passwordValue,
                            rememberValue = true
                        )
                    }
                }
            }
        }
    }

    /**
     * Enumeración que representa todos los campos del formulario de inicio de sesión.
     */
    enum class Field {
        EMAIL,
        PASSWORD
    }

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param field Campo de entrada que ha cambiado.
     * @param value Nuevo valor del campo de entrada.
     */
    fun onFieldChanged(
        field: Field,
        value: String
    ) {
        // Eliminar el caracter de nueva línea del valor original
        // NOTA. Esto se hace por si se hubiese pulsado la tecla Enter del teclado
        val cleanedValue = value.replace("\n", "")

        // Actualizamos el campo correspondiente
        _loginUiState.update {
            when (field) {
                Field.EMAIL -> it.copy(emailValue = cleanedValue)
                Field.PASSWORD -> it.copy(passwordValue = cleanedValue)
            }
        }

        // Si está marcado el toggle de recordar, guardamos en preferencias
        if (loginUiState.value.rememberValue) {
            viewModelScope.launch {
                userPreferencesRepository.saveUserPreferences(
                    emailValue = loginUiState.value.emailValue,
                    passwordValue = loginUiState.value.passwordValue,
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
                passwordValue = if(rememberValue) loginUiState.value.passwordValue else "",
                rememberValue = rememberValue
            )
        }
    }

    fun signInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        navigateToHome: () -> Unit = {}
    ) {
        // Indicar que se está cargando
        _loadingState.value = true

        viewModelScope.launch {
            try{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        // Termina de cargar
                        _loadingState.value = false

                        if(task.isSuccessful) {
                            navigateToHome()
                        } else {
                            _errorMessage.value = context.getString(R.string.error_login_label)
                        }
                    }
            } catch(_: Exception) {}
        }
    }

    fun signInWithGoogleCredential(
        credential: AuthCredential,
        navigateToHome: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try{
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful) {
                            navigateToHome()
                        }
                    }
            } catch(_: Exception) {}
        }
    }

    companion object {
        /**
         * Función para validar el formulario de inicio de sesión.
         *
         * @param loginUiState Estado actual de la interfaz de inicio de sesión.
         * @return true si el formulario es válido, false en caso contrario.
         */
        fun validateForm(
            loginUiState: LoginUiState
        ): Boolean {
            return ValidationUtils.isEmailValid(loginUiState.emailValue) &&
                    ValidationUtils.isPasswordValid(loginUiState.passwordValue)
        }
    }
}