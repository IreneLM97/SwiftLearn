package com.example.swiftlearn.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.EMAIL_VALUE
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.PASSWORD_VALUE
import com.example.swiftlearn.data.datastore.UserPreferencesKeys.REMEMBER_VALUE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Clase de datos que representa las preferencias del usuario.
 *
 * @param email Email del usuario.
 * @param password Contraseña del usuario.
 * @param remember Boolean que indica si el usuario quiere ser recordado o no.
 */
data class UserPreferences(
    val email: String = "",
    val password: String = "",
    val remember: Boolean = false
)

/**
 * Objeto que contiene claves para las preferencias del usuario.
 */
object UserPreferencesKeys {
    val EMAIL_VALUE = stringPreferencesKey("email_value")
    val PASSWORD_VALUE = stringPreferencesKey("password_value")
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
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @param remember Preferencia que indica si quiere ser recordado o no.
     */
    suspend fun saveUserPreferences(
        email: String,
        password: String,
        remember: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[EMAIL_VALUE] = email
            preferences[PASSWORD_VALUE] = password
            preferences[REMEMBER_VALUE] = remember.toString()

        }
    }

    /**
     * Flujo que emite las preferencias del usuario y maneja errores, como la falta de acceso a las preferencias.
     */
    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserPreferences(
                email = preferences[EMAIL_VALUE] ?: "",
                password = preferences[PASSWORD_VALUE] ?: "",
                remember = (preferences[REMEMBER_VALUE]?.toBoolean() ?: false)
            )
        }
}
