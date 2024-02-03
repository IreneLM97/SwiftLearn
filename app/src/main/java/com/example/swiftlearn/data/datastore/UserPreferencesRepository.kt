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
 * @param rememberUser Boolean que indica si el usuario quiere ser recordado o no.
 * @param emailUser Email del usuario.
 */
data class UserPreferences(
    val rememberUser: Boolean = false,
    val emailUser: String = ""
)

/**
 * Objeto que contiene claves para las preferencias del usuario.
 */
object UserPreferencesKeys {
    const val TAG = "UserPreferencesRepo"
    val REMEMBER_VALUE = stringPreferencesKey("remember_value")
    val EMAIL_VALUE = stringPreferencesKey("email_value")
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
     * @param rememberUser Preferencia que indica si quiere ser recordado o no.
     * @param emailUser Email del usuario.
     */
    suspend fun saveUserPreferences(
        rememberUser: Boolean,
        emailUser: String
    ) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_VALUE] = rememberUser.toString()
            preferences[EMAIL_VALUE] = emailUser
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
                rememberUser = (preferences[REMEMBER_VALUE]?.toBoolean() ?: "false") as Boolean,
                emailUser = preferences[EMAIL_VALUE] ?: ""
            )
        }
}
