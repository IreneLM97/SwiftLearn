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
 * [MapViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de mapas.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
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
                // Obtenemos los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString())
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
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

    /**
     * Función para actualizar las coordenadas de búsqueda y cargar los profesores cercanos.
     *
     * @param searchQuery Consulta de búsqueda actual.
     * @param context Contexto de la aplicación.
     */
    fun updateSearchCoordinates(searchQuery: String, context: Context) {
        viewModelScope.launch {
            // Buscamos coordenadas del lugar buscado
            val searchCoordinates = getCoordinates(searchQuery, context)
            searchCoordinates?.let {
                // Actualizamos el estado de la pantalla con las coordenadas
                _mapUiState.update { it.copy(searchCoordinates = searchCoordinates, isLoading = false) }
                // Cargamos la lista de profesores cercanos
                loadNearbyProfessors(searchCoordinates)
            }

        }
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

    /**
     * Función para cargar los profesores que se encuentren cerca de la ubicación de búsqueda.
     *
     * @param location Coordenadas de la ubicación de búsqueda.
     */
    private fun loadNearbyProfessors(location: LatLng?) {
        viewModelScope.launch {
            try {
                // Obtenemos las coordenadas actuales o (0, 0) si las coordenadas son nulas
                val currentLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

                // Inicializamos una lista mutable para almacenar los profesores cercanos
                val nearbyProfessors = mutableListOf<User>()

                // Filtramos la lista de profesores basándonos en la distancia
                _mapUiState.value.professorsList.forEach { professor ->
                    val professorLatLng = LatLng(professor.latitude, professor.longitude)
                    if (calculateDistance(currentLatLng, professorLatLng) <= 25.0) {
                        nearbyProfessors.add(professor)
                    }
                }

                // Actualizamos el estado de la interfaz con la lista de profesores cercanos al lugar
                _mapUiState.update { it.copy(nearbyProfessors = nearbyProfessors) }
            } catch (_: SecurityException) {}
        }
    }

    /**
     * Calcula la distancia en kilómetros entre dos ubicaciones geográficas utilizando la fórmula de Haversine.
     *
     * @param location1 Primera ubicación geográfica.
     * @param location2 Segunda ubicación geográfica.
     * @return Distancia en kilómetros entre las dos ubicaciones.
     */
    private fun calculateDistance(
        location1: LatLng,
        location2: LatLng
    ): Double {
        // Radio de la Tierra en kilómetros
        val earthRadius = 6371

        // Convertimos las coordenadas de grados a radianes
        val lat1Rad = Math.toRadians(location1.latitude)
        val lon1Rad = Math.toRadians(location1.longitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val lon2Rad = Math.toRadians(location2.longitude)

        // Calculamos a diferencia de latitud y longitud entre las dos ubicaciones
        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        // Aplicamos la fórmula de Haversine para calcular la distancia
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}