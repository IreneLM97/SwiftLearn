package com.example.swiftlearn.ui.screens.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                val user = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString())
                // Actualizar el estado de la pantalla con los datos del usuario obtenidos
                _mapUiState.update { it.copy(user = user ?: User()) }
            } catch (_: Exception) {}

            // Actualizamos estado de cargando a false
            _mapUiState.update { it.copy(isLoading = false) }
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

    // Función para buscar coordenadas a partir de una dirección
    fun searchCoordinates(searchQuery: String, context: Context) {
        val geocoder = Geocoder(context)

        viewModelScope.launch {
            // Intentamos obtener las coordenadas desde la dirección proporcionada
            val addresses = geocoder.getFromLocationName(searchQuery, 1)
            if (!addresses.isNullOrEmpty()) {
                val lat = addresses[0].latitude
                val lng = addresses[0].longitude
                // Actualizamos el estado con las coordenadas obtenidas
                _mapUiState.update { it.copy(searchCoordinates = lat to lng, isLoading = false) }
            } else {
                // Si no se encontraron coordenadas, marcamos isLoading como false para detener el indicador de carga
                _mapUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}