package com.example.swiftlearn.ui.screens.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    // Lista de direcciones de los profesores
    private var professorsAddresses = mutableListOf<String>()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtener los datos del usuario desde el repositorio
                val user = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
                _mapUiState.update { it.copy(user = user ?: User()) }

                // Obtenemos el flujo de datos de los profesores
                userRepository.getAllProfessors().collect { professors ->
                    professorsAddresses.addAll(professors.map { it.address })
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
        val searchCoordinates = searchCoordinates(searchQuery, context)
        _mapUiState.update { it.copy(searchCoordinates = searchCoordinates, isLoading = false) }
    }

    // Función para buscar coordenadas a partir de una dirección
    private fun searchCoordinates(searchQuery: String, context: Context): LatLng? {
        val geocoder = Geocoder(context)

        // Intentamos obtener las coordenadas desde la dirección proporcionada
        val addresses = geocoder.getFromLocationName(searchQuery, 1)
        if (!addresses.isNullOrEmpty()) {
            val lat = addresses[0].latitude
            val lng = addresses[0].longitude
            return LatLng(lat, lng)
        }

        // Si no se encontraron coordenadas, marcamos isLoading como false para detener el indicador de carga
        _mapUiState.update { it.copy(isLoading = false) }
        return null
    }

    /**
     * Función para cargar las direcciones de los profesores y mostrar las que están dentro de 10 km de distancia.
     *
     * @param context Contexto de la aplicación.
     */
    fun loadProfessorsAddresses(context: Context) {
        try {
            val location = _mapUiState.value.searchCoordinates
            val currentLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

            val nearbyProfessors = mutableListOf<User>()
            val nearbyCoordinates = mutableListOf<LatLng>()

            // Filtrar la lista de profesores basándose en la distancia
            professorsAddresses.forEach { address ->
                val professorLatLng = searchCoordinates(address, context)
                if (professorLatLng != null && calculateDistance(currentLatLng, professorLatLng) <= 25.0) {
                    nearbyCoordinates.add(professorLatLng)
                    val professor = _mapUiState.value.professorsList.find { it.address == address }
                    professor?.let { nearbyProfessors.add(it) }
                }
            }

            // Actualizar el estado con la lista de profesores cerca del lugar
            _mapUiState.update { it.copy(nearbyProfList = nearbyProfessors, nearbyProfCoordinates = nearbyCoordinates) }
        } catch (_: SecurityException) {}
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