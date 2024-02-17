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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.SearchTextField
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de mapas
    val mapUiState = viewModel.mapUiState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    // Proveedor de servicios de ubicación
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Estado del permiso de ubicación
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Iniciamos la posición de la cámara en Madrid
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(LatLng(40.4168, -3.7038), 14f)
    }

    // Efecto de lanzamiento para actualizar la posición de la cámara cuando cambien las coordenadas
    LaunchedEffect(mapUiState.searchCoordinates) {
        mapUiState.searchCoordinates?.let { coordinates ->
            val cameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(coordinates.first, coordinates.second),
                14f
            )
            cameraPositionState.position = cameraPosition
        }
    }

    // Efecto de lanzamiento para solicitar permisos de ubicación si no los tuviera
    LaunchedEffect(locationPermissionState) {
        if (locationPermissionState.hasPermission) {
            // Si tiene permisos de ubicación, enfocamos la cámara en la última ubicación conocida del usuario
            val location = fusedLocationProvider.lastLocation.await()
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
            }
        } else {
            // Si no tiene permisos, entonces solicitamos permisos de ubicación al usuario
            locationPermissionState.launchPermissionRequest()
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
        // Mostramos el contenido del mapa
        MapContent(
            cameraPositionState = cameraPositionState,
            mapUiState = mapUiState,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {
                viewModel.searchCoordinates(it, context)
            }
        )
    }
}

@Composable
fun MapContent(
    cameraPositionState: CameraPositionState,
    mapUiState: MapUiState = MapUiState(),
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
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
            // Si hay coordenadas, agregamos un marcador en esa ubicación
            mapUiState.searchCoordinates?.let { (lat, lng) ->
                Marker(
                    position = LatLng(lat, lng),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
                // Mostramos un texto en el marcador
                MarkerInfoWindowContent(
                    position = LatLng(lat, lng),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                ) {
                    Text(
                        text = mapUiState.searchQuery
                    )
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