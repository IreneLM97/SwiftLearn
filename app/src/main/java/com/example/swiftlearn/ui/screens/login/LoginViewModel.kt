package com.example.swiftlearn.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.R
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [LoginViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de inicio de sesión.
 *
 * @param userPreferencesRepository Repositorio para acceder a las preferencias del usuario.
 * @param userRepository Repositorio para gestionar la colección usuarios.
 */
class LoginViewModel(
    val userPreferencesRepository: UserPreferencesRepository,
    val userRepository: UserRepository
) : ViewModel() {
    // Estado de la interfaz de inicio de sesión
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            // Observamos los cambios en las preferencias del usuario
            userPreferencesRepository.userPreferences.collect {userPreferences ->
                if(userPreferences.remember) {
                    _loginUiState.update {
                        val loginDetails = LoginDetails(userPreferences.email, userPreferences.password)
                        it.copy(
                            loginDetails = loginDetails,
                            remember = true,
                            isEntryValid = validateForm(loginDetails)
                        )
                    }
                }
            }
        }
    }

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param loginDetails Detalles del formulario.
     */
    fun onFieldChanged(loginDetails: LoginDetails) {
        _loginUiState.update {it.copy(loginDetails = loginDetails, isEntryValid = validateForm(loginDetails)) }

        // Si está marcado el toggle de recordar, guardamos en preferencias
        if (loginUiState.value.remember) {
            viewModelScope.launch {
                userPreferencesRepository.saveUserPreferences(
                    email = loginUiState.value.loginDetails.email,
                    password = loginUiState.value.loginDetails.password,
                    remember = true
                )
            }
        }
    }

    /**
     * Función que se ejecuta cuando cambia el toggle.
     *
     * @param remember Valor del toggle que indica si quiere ser recordado o no.
     */
    fun onToggleChanged(remember: Boolean = false) {
        // Actualizamos el estado de la página
        _loginUiState.update { it.copy(remember = remember) }

        // Actualizamos las preferencias del usuario
        viewModelScope.launch {
            userPreferencesRepository.saveUserPreferences(
                email = if(remember) loginUiState.value.loginDetails.email else "",
                password = if(remember) loginUiState.value.loginDetails.password else "",
                remember = remember
            )
        }
    }

    /**
     * Función para iniciar sesión con correo electrónico y contraseña.
     *
     * @param context Contexto de la aplicación.
     * @param loginDetails Detalles del inicio de sesión (correo electrónico y contraseña).
     * @param navigateToHome Función de navegación para ir a la pantalla principal.
     */
    fun signInWithEmailAndPassword(
        context: Context,
        loginDetails: LoginDetails,
        navigateToHome: () -> Unit = {}
    ) {
        // Actualizar estado de cargando a true
        _loginUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try{
                // Iniciamos sesión con correo electrónico y contraseña
                auth.signInWithEmailAndPassword(loginDetails.email, loginDetails.password)
                    .addOnCompleteListener{task ->
                        // Actualizar estado de cargando a false
                        _loginUiState.update { it.copy(isLoading = false) }

                        if(task.isSuccessful) {
                            // Si ha podido iniciar sesión, navegamos a Home
                            navigateToHome()
                        } else {
                            // Si no ha podido iniciar sesión, mostramos mensaje de error
                            _loginUiState.update { it.copy(errorMessage = context.getString(R.string.error_login_label)) }
                        }
                    }
            } catch(_: Exception) {}
        }
    }

    /**
     * Función para iniciar sesión con credenciales de Google.
     *
     * @param credential Credencial de autenticación.
     * @param navigateToHome Función de navegación para ir a la pantalla principal.
     */
    fun signInWithGoogleCredential(
        credential: AuthCredential,
        navigateToHome: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try{
                // Iniciamos sesión con la credencial de Google
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful) {
                            // Si ha podido iniciar sesión, creamos objeto User con los datos
                            viewModelScope.launch {
                                userRepository.insertUser(
                                    User(
                                        authId = auth.currentUser?.uid.toString(),
                                        email = auth.currentUser?.email.toString(),
                                        role = Role.Alumno
                                    )
                                )
                            }
                            // Navegamos a Home
                            navigateToHome()
                        }
                    }
            } catch(_: Exception) {}
        }
    }

    /**
     * Función para validar el formulario de inicio de sesión.
     *
     * @param loginDetails Datos del inicio de sesión recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(loginDetails: LoginDetails): Boolean {
        return ValidationUtils.isEmailValid(loginDetails.email) &&
                ValidationUtils.isPasswordValid(loginDetails.password)
    }
}