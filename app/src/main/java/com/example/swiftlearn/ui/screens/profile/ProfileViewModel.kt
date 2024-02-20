package com.example.swiftlearn.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Role
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
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [ProfileViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de perfil de usuario.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 * @param favoriteRepository Repositorio para gestionar la colección favoritos.
 * @param requestRepository Repositorio para gestionar la colección solicitudes.
 */
class ProfileViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de perfil
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
                _profileUiState.update {
                    val profileDetails = userLogged?.toProfileDetails() ?: ProfileDetails()
                    it.copy(
                        userLogged = userLogged ?: User(),
                        profileDetails = profileDetails,
                        isEntryValid = validateForm(profileDetails)
                    )
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param profileDetails Detalles del formulario.
     */
    fun onFieldChanged(profileDetails: ProfileDetails) {
        _profileUiState.update { it.copy(profileDetails = profileDetails, isEntryValid = validateForm(profileDetails)) }
    }

    /**
     * Función que actualiza un usuario con nuevos datos.
     *
     * @param user Usuario con los datos actualizados.
     * @param context Contexto de la aplicación.
     */
    fun updateUser(
        user: User,
        context: Context
    ) {
        // Actualizamos estado de cargando a true
        _profileUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Actualizamos las coordenadas del usuario
            val coordinates = getCoordinates(user.address, context)
            val userWithCoordinates = user.copy(latitude = coordinates?.latitude ?: 0.0, longitude = coordinates?.longitude ?: 0.0)

            // Actualizamos los datos del usuario en la colección
            userRepository.updateUser(userWithCoordinates)

            // Actualizamos estado de cargando a false
            _profileUiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Función para eliminar un usuario y todas sus referencias asociadas.
     *
     * @param user Usuario a eliminar.
     * @param navigateToLogin Función de navegación para ir a la pantalla de inicio de sesión.
     */
    fun deleteUser(
        user: User,
        navigateToLogin: () -> Unit
    ) {
        // Actualizamos estado de cargando a true
        _profileUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Eliminamos el usuario de la autenticación
                auth.currentUser?.delete()?.await()

                // Eliminamos dependencias según el rol del usuario
                if (user.role == Role.Profesor) {
                    val advertsIds = advertRepository.getAdvertsIdsByProfId(user._id)
                    favoriteRepository.deleteAllFavoritesByAdvertIds(advertsIds)
                    requestRepository.deleteAllRequestsByAdvertIds(advertsIds)
                    advertRepository.deleteAllAdvertsByProfId(user._id)
                } else {
                    favoriteRepository.deleteAllFavoritesByStudentId(user._id)
                    requestRepository.deleteAllRequestsByStudentId(user._id)
                }

                // Eliminamos el usuario de la colección
                userRepository.deleteUser(user)

                // Actualizamos estado de cargando a false
                _profileUiState.update { it.copy(isLoading = false) }

                // Navegamos a Login
                navigateToLogin()
            } catch (_: Exception) {}
        }
    }

    /**
     * Función para cerrar la sesión del usuario actual.
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Función para validar el formulario de perfil.
     *
     * @param profileDetails Datos del usuario recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(profileDetails: ProfileDetails): Boolean {
        return profileDetails.username.trim().isNotEmpty() &&
                ValidationUtils.isPhoneValid(profileDetails.phone) &&
                profileDetails.address.trim().isNotEmpty() &&
                ValidationUtils.isPostalValid(profileDetails.postal)
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