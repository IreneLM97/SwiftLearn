package com.example.swiftlearn.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
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
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de perfil.
 */
class ProfileViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository
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
                // Obtener los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
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

    fun onFieldChanged(profileDetails: ProfileDetails) {
        _profileUiState.update { it.copy(profileDetails = profileDetails, isEntryValid = validateForm(profileDetails)) }
    }

    fun updateUser(user: User, context: Context) {
        // Actualizar estado de cargando a true
        _profileUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Actualizamos las coordenadas del usuario
            val coordinates = saveCoordinates(user.address, context)
            val userWithCoordinates = user.copy(latitude = coordinates?.latitude.toString(), longitude = coordinates?.longitude.toString())

            // Actualizamos los datos del usuario
            userRepository.updateUser(userWithCoordinates)

            // Actualizar estado de cargando a false
            _profileUiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteUser(user: User) {
        // Actualizar estado de cargando a true
        _profileUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Eliminamos todos los favoritos asociados a los anuncios de este usuario
                val advertsIds = advertRepository.getAdvertsIdsByProfId(user._id)
                favoriteRepository.deleteAllFavoritesByAdvertIds(advertsIds)

                // Eliminamos todos los anuncios de ese usuario
                advertRepository.deleteAllAdvertsByProfId(user._id)

                // Eliminamos todos los favoritos de ese usuario
                favoriteRepository.deleteAllFavoritesByUserId(user._id)

                // Eliminamos el usuario de la base de datos
                userRepository.deleteUser(user)

                // Eliminamos el usuario de la autentificación
                auth.currentUser?.delete()

                // Actualizar estado de cargando a false
                _profileUiState.update { it.copy(isLoading = false) }
            } catch (_: Exception) {}
        }
    }

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