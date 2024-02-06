package com.example.swiftlearn.ui.screens.register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de registro.
 */
class RegisterViewModel : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Estado para almacenar el mensaje de error si no se puede registrar
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado para indicar si se está realizando el proceso de registro
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    /**
     * Enumeración que representa todos los campos del formulario de registro.
     */
    enum class Field {
        ROL,
        USERNAME,
        PHONE,
        ADDRESS,
        POSTALCODE,
        EMAIL,
        PASSWORD,
        CONFIRMPASSWORD
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
        _registerUiState.update {
            when (field) {
                Field.ROL -> it.copy(rolValue = if (cleanedValue == "Profesor") Rol.Profesor else Rol.Alumno)
                Field.USERNAME -> it.copy(usernameValue = cleanedValue)
                Field.PHONE -> it.copy(phoneValue = cleanedValue)
                Field.ADDRESS -> it.copy(addressValue = cleanedValue)
                Field.POSTALCODE -> it.copy(postalValue = cleanedValue)
                Field.EMAIL -> it.copy(emailValue = cleanedValue)
                Field.PASSWORD -> it.copy(passwordValue = cleanedValue)
                Field.CONFIRMPASSWORD -> it.copy(confirmPasswordValue = cleanedValue)
            }
        }
    }

    fun createUserWithEmailAndPassword(
        context: Context,
        user: User,
        navigateToHome: () -> Unit = {}
    ) {
        if(!_loadingState.value) {
            _loadingState.value = true

            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {task ->
                    // Termina de cargar
                    _loadingState.value = false

                    if(task.isSuccessful) {
                        createUser(user)
                        navigateToHome()
                    } else {
                        _errorMessage.value = context.getString(R.string.error_register_label)
                    }
                }

        }
    }

    private fun createUser(
        user: User
    ) {
        // Recogemos el Id que se generó al autentificar al usuario
        val id = auth.currentUser?.uid

        // Asignamos el Id al usuario
        val userWithId = user.copy(id = id.toString())

        // Agregamos la información del usuario a la colección
        FirebaseFirestore.getInstance().collection("users")
            .add(userWithId.toMap())
    }

    companion object {
        /**
         * Función para validar el formulario de registro.
         *
         * @param registerUiState Estado actual de la interfaz de registro.
         * @return true si el formulario es válido, false en caso contrario.
         */
        fun validateForm(
            registerUiState: RegisterUiState
        ): Boolean {
            return registerUiState.usernameValue.trim().isNotEmpty() &&
                    ValidationUtils.isPhoneValid(registerUiState.phoneValue) &&
                    registerUiState.addressValue.trim().isNotEmpty() &&
                    ValidationUtils.isPostalValid(registerUiState.postalValue) &&
                    ValidationUtils.isEmailValid(registerUiState.emailValue) &&
                    ValidationUtils.isPasswordValid(registerUiState.passwordValue) &&
                    ValidationUtils.isConfirmPasswordValid(registerUiState.passwordValue, registerUiState.confirmPasswordValue)
        }
    }
}