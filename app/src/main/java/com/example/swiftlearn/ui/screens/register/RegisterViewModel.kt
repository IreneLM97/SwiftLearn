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
 * [RegisterViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de registro.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 */
class RegisterViewModel(
    val userRepository: UserRepository
) : ViewModel() {
    // Estado de la interfaz de registro
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param registerDetails Detalles del formulario.
     */
    fun onFieldChanged(registerDetails: RegisterDetails) {
        _registerUiState.update { it.copy(registerDetails = registerDetails, isEntryValid = validateForm(registerDetails)) }
    }

    /**
     * Función para crear un nuevo usuario y agregarlo a la autentificación Firebase.
     *
     * @param context Contexto de la aplicación.
     * @param registerDetails Detalles del registro del usuario.
     * @param navigateToHome Función de navegación para ir a la pantalla principal.
     */
    fun createUserWithEmailAndPassword(
        context: Context,
        registerDetails: RegisterDetails,
        navigateToHome: () -> Unit = {}
    ) {
        // Comprobamos que no está cargando para no registrar varios usuarios al mismo tiempo
        if(!_registerUiState.value.isLoading) {
            // Actualizamos estado de cargando a true
            _registerUiState.update { it.copy(isLoading = true) }

            // Creamos la autentificación en Firebase del nuevo usuario
            auth.createUserWithEmailAndPassword(registerDetails.email, registerDetails.password)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful) {
                        viewModelScope.launch {
                            // Insertamos el usuario en la colección
                            insertUser(registerDetails.toUser(), context)

                            // Actualizamos estado de cargando a false
                            _registerUiState.update { it.copy(isLoading = false) }

                            // Navegamos a Home
                            navigateToHome()
                        }
                    } else {
                        // Actualizamos estado de cargando a false
                        _registerUiState.update { it.copy(isLoading = false) }

                        // Actualizamos mensaje de error
                        _registerUiState.update { it.copy(errorMessage = context.getString(R.string.error_register_label)) }
                    }
                }
        }
    }

    /**
     * Función para insertar un nuevo usuario.
     *
     * @param user Usuario a insertar.
     * @param context Contexto de la aplicación.
     */
    private suspend fun insertUser(
        user: User,
        context: Context
    ) {
        // Recogemos el Id que se generó al autentificar al usuario
        val authId = auth.currentUser?.uid

        // Asignamos el Id al usuario
        val userWithAuthId = user.copy(authId = authId.toString())

        // Asignamos coordenadas de la dirección al usuario
        val coordinates = getCoordinates(user.address, context)
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

    /**
     * Función para obtener las coordenadas geográficas de una ubicación a partir de una dirección.
     *
     * @param address Dirección proporcionada para obtener las coordenadas.
     * @param context Contexto de la aplicación.
     * @return Coordenadas geográficas de la ubicación encontrada, o null si no se encuentran coordenadas.
     */
    private suspend fun getCoordinates(address: String, context: Context): LatLng? {
        // Creamos cliente de Places utilizando el contexto proporcionado
        val placesClient = Places.createClient(context)
        // Definimos los campos que queremos recuperar de la respuesta de búsqueda
        val fields = listOf(Place.Field.LAT_LNG)

        // Creamos una solicitud de predicciones de autocompletado con la consulta de búsqueda
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(address)
            .build()

        return suspendCoroutine { continuation ->
            // Realizamos la búsqueda de predicciones y manejamos el resultado obtenido
            placesClient.findAutocompletePredictions(request)
                .addOnCompleteListener { task: Task<FindAutocompletePredictionsResponse> ->
                    if (task.isSuccessful) {
                        // Verificamos si se obtuvo una respuesta válida y si hay predicciones disponibles
                        val response = task.result
                        if (response != null && !response.autocompletePredictions.isNullOrEmpty()) {
                            // Obtenemos la primera predicción de la lista
                            val prediction = response.autocompletePredictions[0]
                            val placeId = prediction.placeId

                            // Utilizamos el placeId para obtener los detalles que queremos del lugar
                            placesClient.fetchPlace(
                                FetchPlaceRequest.newInstance(placeId, fields)
                            ).addOnCompleteListener { fetchTask: Task<FetchPlaceResponse> ->
                                if (fetchTask.isSuccessful) {
                                    // Obtenemos las coordenadas geográficas del lugar
                                    val place = fetchTask.result?.place
                                    val latLng = place?.latLng
                                    // Continuamos la ejecución con las coordenadas obtenidas
                                    continuation.resume(latLng)
                                }
                            }
                        }
                    } else {
                        // Si la tarea no fue exitosa, continuamos la ejecución con un valor nulo
                        continuation.resume(null)
                    }
                }
        }
    }
}