package com.example.swiftlearn

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.swiftlearn.data.datastore.UserPreferencesRepository
import com.example.swiftlearn.di.AppContainer
import com.example.swiftlearn.di.AppDataContainer
import com.google.android.libraries.places.api.Places

/**
 * Nombre del archivo de preferencias de la aplicación.
 */
private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"

/**
 * DataStore utilizado para almacenar preferencias de la aplicación.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

/**
 * Clase [SwiftLearnApplication] que representa la aplicación de Android.
 *
 * Esta clase extiende [Application] y se utiliza para realizar inicializaciones globales.
 */
class SwiftLearnApplication : Application() {
    companion object {
        const val FCM_CHANNEL_ID = "FCM_CHANNEL_ID"
    }

    // Instancia de [AppContainer] utilizada por el resto de las clases para obtener dependencias
    lateinit var container: AppContainer

    // Repositorio de preferencias del usuario que se utiliza para gestionar las preferencias de la aplicación
    lateinit var userPreferencesRepository: UserPreferencesRepository

    /**
     * Método llamado cuando la aplicación se está iniciando.
     * Se encarga de realizar las inicializaciones necesarias.
     */
    override fun onCreate() {
        super.onCreate()

        // Inicialización del contenedor de la aplicación y del repositorio de preferencias del usuario
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val fcmChannel = NotificationChannel(FCM_CHANNEL_ID, "FCM_CHANNEL", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(fcmChannel)
        }
    }
}
