package com.example.swiftlearn.ui.screens.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val fusedLocationProvider =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(LatLng(0.0, 0.0), 13f, 0f, 0f)
    }

    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Obtener la ubicación del usuario y actualizar la posición de la cámara
    LaunchedEffect(locationPermissionState) {
        if (locationPermissionState.hasPermission) {
            try {
                val location = fusedLocationProvider.lastLocation.await()
                location?.let {
                    val latLng = LatLng(location.latitude, location.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 13f)
                }
                Log.e("Location", location.toString())
            } catch (e: Exception) {
                Log.e("MapScreen", "Error obtaining location: ${e.message}")
            }
        } else if (locationPermissionState.shouldShowRationale) {
            // Handle rationale here if needed
        } else {
            // Request permission
            locationPermissionState.launchPermissionRequest()
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(),

            )

        Text(text = "Map")
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen()
}