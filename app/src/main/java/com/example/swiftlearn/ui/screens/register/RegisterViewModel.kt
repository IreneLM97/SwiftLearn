package com.example.swiftlearn.ui.screens.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.R
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de registro.
 */
class RegisterViewModel(
    val userRepository: UserRepository
) : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    fun onFieldChanged(registerDetails: RegisterDetails) {
        _registerUiState.update { it.copy(registerDetails = registerDetails, isEntryValid = validateForm(registerDetails)) }
    }

    fun createUserWithEmailAndPassword(
        context: Context,
        registerDetails: RegisterDetails,
        navigateToHome: () -> Unit = {}
    ) {
        if(!_registerUiState.value.isLoading) {
            // Actualizar estado de cargando a true
            _registerUiState.update { it.copy(isLoading = true) }

            auth.createUserWithEmailAndPassword(registerDetails.email, registerDetails.password)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful) {
                        viewModelScope.launch {
                            insertUser(registerDetails.toUser(), context)

                            // Actualizamos el estado de cargando a false
                            _registerUiState.update { it.copy(isLoading = false) }

                            // Navegamos a Home
                            navigateToHome()
                        }
                    } else {
                        // Actualizar estado de cargando a false
                        _registerUiState.update { it.copy(isLoading = false) }

                        // Actualizar mensaje de error
                        _registerUiState.update { it.copy(errorMessage = context.getString(R.string.error_register_label)) }
                    }
                }
        }
    }

    private suspend fun insertUser(user: User, context: Context) {
        // Recogemos el Id que se generó al autentificar al usuario
        val authId = auth.currentUser?.uid

        // Asignamos el Id al usuario
        val userWithAuthId = user.copy(authId = authId.toString())

        // Asignamos coordenadas de la dirección al usuario
        val coordinates = saveCoordinates(user.address, context)
        val userWithCoordinates = userWithAuthId.copy(latitude = coordinates?.latitude ?: 0.0, longitude = coordinates?.longitude ?: 0.0)

        // Agregamos el usuario a la colección
        userRepository.insertUser(userWithCoordinates)
    }

    /**
     * Función para validar el formulario de registro.
     *
     * @param registerDetails Datos del usuario recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(registerDetails: RegisterDetails): Boolean {
        return registerDetails.username.trim().isNotEmpty() &&
                ValidationUtils.isPhoneValid(registerDetails.phone) &&
                registerDetails.address.trim().isNotEmpty() &&
                ValidationUtils.isPostalValid(registerDetails.postal) &&
                ValidationUtils.isEmailValid(registerDetails.email) &&
                ValidationUtils.isPasswordValid(registerDetails.password) &&
                ValidationUtils.isConfirmPasswordValid(registerDetails.password, registerDetails.confirmPassword)
    }

    private suspend fun saveCoordinates(address: String, context: Context): LatLng? {
        val placesClient = Places.createClient(context)
        val fields = listOf(Place.Field.LAT_LNG)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(address)
            .build()

        return suspendCoroutine { continuation ->
            placesClient.findAutocompletePredictions(request)
                .addOnCompleteListener { task: Task<FindAutocompletePredictionsResponse> ->
                    if (task.isSuccessful) {
                        val response = task.result
                        if (response != null && !response.autocompletePredictions.isNullOrEmpty()) {
                            val prediction = response.autocompletePredictions[0]
                            val placeId = prediction.placeId

                            placesClient.fetchPlace(
                                FetchPlaceRequest.newInstance(placeId, fields)
                            ).addOnCompleteListener { fetchTask: Task<FetchPlaceResponse> ->
                                if (fetchTask.isSuccessful) {
                                    val place = fetchTask.result?.place
                                    val latLng = place?.latLng
                                    continuation.resume(latLng)
                                }
                            }
                        }
                    } else {
                        continuation.resume(null)
                    }
                }
        }
    }
}