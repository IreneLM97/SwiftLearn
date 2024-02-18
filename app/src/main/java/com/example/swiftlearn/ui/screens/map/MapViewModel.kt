package com.example.swiftlearn.ui.screens.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de mapas.
 */
class MapViewModel(
    userRepository: UserRepository
): ViewModel() {
    // Estado de la interfaz de mapas
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState = _mapUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtener los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
                _mapUiState.update { it.copy(userLogged = userLogged ?: User()) }

                // Obtenemos el flujo de datos de los profesores
                userRepository.getAllProfessors().collect { professors ->
                    _mapUiState.update { it.copy(professorsList = professors) }

                    // Actualizamos estado de cargando a false
                    _mapUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función que se ejecuta cuando cambia la consulta de búsqueda.
     *
     * @param searchQuery Consulta de búsqueda actual.
     */
    fun onQueryChange(searchQuery: String) {
        _mapUiState.update { it.copy(searchQuery = searchQuery) }
    }

    fun updateSearchCoordinates(searchQuery: String, context: Context) {
        viewModelScope.launch {
            val searchCoordinates = searchCoordinates(searchQuery, context)
            searchCoordinates?.let {
                _mapUiState.update { it.copy(searchCoordinates = searchCoordinates, isLoading = false) }
                loadNearbyProfessors(searchCoordinates)
            }

        }
    }

    private suspend fun searchCoordinates(searchQuery: String, context: Context): LatLng? {
        val placesClient = Places.createClient(context)
        val fields = listOf(Place.Field.LAT_LNG)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(searchQuery)
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

    /**
     * Función para cargar los profesores que se encuentren cerca de la ubicación de búsqueda.
     *
     */
    private fun loadNearbyProfessors(location: LatLng?) {
        viewModelScope.launch {
            try {
                val currentLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

                val nearbyProfessors = mutableListOf<User>()

                // Filtrar la lista de profesores basándose en la distancia
                _mapUiState.value.professorsList.forEach { professor ->
                    val professorLatLng = LatLng(
                        professor.latitude.toDoubleOrNull() ?: 0.0,
                        professor.longitude.toDoubleOrNull() ?: 0.0
                    )
                    if (calculateDistance(currentLatLng, professorLatLng) <= 25.0) {
                        nearbyProfessors.add(professor)
                    }
                }

                // Actualizar el estado con la lista de profesores cerca del lugar
                _mapUiState.update { it.copy(nearbyProfessors = nearbyProfessors) }
            } catch (_: SecurityException) {
            }
        }
    }

    private fun calculateDistance(
        location1: LatLng,
        location2: LatLng
    ): Double {
        val r = 6371
        val dLat = Math.toRadians(location2.latitude - location1.latitude)
        val dLon = Math.toRadians(location2.longitude - location1.longitude)
        val lat1Rad = Math.toRadians(location1.latitude)
        val lat2Rad = Math.toRadians(location2.latitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1Rad) * cos(lat2Rad)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }
}