package com.example.swiftlearn.ui.screens.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.SearchTextField
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/**
 * [MapScreen] define la pantalla de visualizado del mapa.
 *
 * @param viewModel ViewModel para gestionar la pantalla de mapas.
 */
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de mapas
    val mapUiState = viewModel.mapUiState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    // Iniciamos la posición de la cámara en la ubicación del usuario autentificado
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.Builder().target(LatLng(mapUiState.userLogged.latitude, mapUiState.userLogged.longitude)).zoom(15f).build()
    }

    // Efecto de lanzamiento para actualizar la posición de la cámara cuando cambien las coordenadas
    LaunchedEffect(mapUiState.searchCoordinates) {
        mapUiState.searchCoordinates?.let { coordinates ->
            val cameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(coordinates.latitude, coordinates.longitude),
                14f
            )
            cameraPositionState.position = cameraPosition
        }
    }

    if(mapUiState.isLoading) {
        // Mostramos el icono cargando si está cargando
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostramos el contenido del mapa si no está cargando
        MapContent(
            cameraPositionState = cameraPositionState,
            mapUiState = mapUiState,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {
                viewModel.updateSearchCoordinates(it, context)
            }
        )
    }
}

/**
 * Función que define el contenido del mapa interactivo.
 *
 * @param cameraPositionState Estado de la posición de la cámara del mapa.
 * @param mapUiState Estado de la interfaz de usuario.
 * @param onQueryChange Función que se ejecuta al cambiar el valor del campo de búsqueda.
 * @param onSearch Función para manejar el evento de búsqueda.
 */
@Composable
fun MapContent(
    cameraPositionState: CameraPositionState,
    mapUiState: MapUiState = MapUiState(),
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    // Caja contenedora del mapa
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Dibujamos el mapa
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            // Si hay coordenadas de búsqueda, agregamos un marcador en esa ubicación
            mapUiState.searchCoordinates?.let { coordinates ->
                Marker(
                    position = LatLng(coordinates.latitude, coordinates.longitude),
                    title = stringResource(R.string.you_are_here)
                )
            }

            // Agregamos marcadores para cada profesor cercano a la ubicación de búsqueda
            mapUiState.nearbyProfessors.forEach { professor ->
                Marker(
                    position = LatLng(professor.latitude, professor.longitude),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )

                // Mostrar el nombre del profesor y su información de contacto en el marcador
                MarkerInfoWindowContent(
                    position = LatLng(professor.latitude, professor.longitude),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                ) {
                    Column {
                        Text(text = professor.username, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = professor.email, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = professor.phone, fontSize = 12.sp)
                    }
                }
            }
        }

        // Campo de búsqueda de ubicación
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            SearchTextField(
                placeholder = stringResource(R.string.search_map_label),
                query = mapUiState.searchQuery,
                onQueryChange = onQueryChange,
                onSearch = onSearch
            )
        }
    }
}