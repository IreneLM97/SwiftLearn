package com.example.swiftlearn.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.R
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de inicio de sesión.
 *
 * @param userPreferencesRepository Repositorio para acceder a las preferencias del usuario.
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

    private fun validateForm(loginDetails: LoginDetails): Boolean {
        return ValidationUtils.isEmailValid(loginDetails.email) &&
                ValidationUtils.isPasswordValid(loginDetails.password)
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

    fun signInWithEmailAndPassword(
        context: Context,
        loginDetails: LoginDetails,
        navigateToHome: () -> Unit = {}
    ) {
        // Indicar que se está cargando
        _loginUiState.update { it.copy(loadingState = true) }

        viewModelScope.launch {
            try{
                auth.signInWithEmailAndPassword(loginDetails.email, loginDetails.password)
                    .addOnCompleteListener{task ->
                        // Termina de cargar
                        _loginUiState.update { it.copy(loadingState = false) }

                        if(task.isSuccessful) {
                            navigateToHome()
                        } else {
                            _loginUiState.update { it.copy(errorMessage = context.getString(R.string.error_login_label)) }
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
                            viewModelScope.launch {
                                userRepository.insertUser(
                                    User(
                                        authId = auth.currentUser?.uid.toString(),
                                        email = auth.currentUser?.email.toString(),
                                        role = Role.Alumno
                                    )
                                )
                            }
                            navigateToHome()
                        }
                    }
            } catch(_: Exception) {}
        }
    }
}