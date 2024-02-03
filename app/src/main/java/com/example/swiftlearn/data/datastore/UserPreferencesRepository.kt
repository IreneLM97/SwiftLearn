package com.example.swiftlearn.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.EMAIL_VALUE
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.REMEMBER_VALUE
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Clase de datos que representa las preferencias del usuario.
 *
 * @param emailValue Email del usuario.
 * @param rememberValue Boolean que indica si el usuario quiere ser recordado o no.
 */
data class UserPreferences(
    val emailValue: String = "",
    val rememberValue: Boolean = false
)

/**
 * Objeto que contiene claves para las preferencias del usuario.
 */
object UserPreferencesKeys {
    const val TAG = "UserPreferencesRepo"
    val EMAIL_VALUE = stringPreferencesKey("email_value")
    val REMEMBER_VALUE = stringPreferencesKey("remember_value")
}

/**
 * Clase que gestiona las preferencias del usuario utilizando DataStore.
 *
 * @param dataStore Almacén de datos para las preferencias del usuario.
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Guarda las preferencias del usuario.
     *
     * @param emailValue Email del usuario.
     * @param rememberValue Preferencia que indica si quiere ser recordado o no.
     */
    suspend fun saveUserPreferences(
        emailValue: String,
        rememberValue: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[EMAIL_VALUE] = emailValue
            preferences[REMEMBER_VALUE] = rememberValue.toString()

        }
    }

    /**
     * Flujo que emite las preferencias del usuario y maneja errores, como la falta de acceso a las preferencias.
     */
    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserPreferences(
                emailValue = preferences[EMAIL_VALUE] ?: "",
                rememberValue = (preferences[REMEMBER_VALUE]?.toBoolean() ?: false)
            )
        }
}
