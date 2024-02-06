package com.example.swiftlearn.di

import android.content.Context
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.data.firestore.users.UserRepositoryImpl

/**
 * Interfaz que define las dependencias de la aplicación.
 */
interface AppContainer {
    val userRepository: UserRepository
}

/**
 * Implementación de [AppContainer] que proporciona instancias concretas de las dependencias de la aplicación.
 *
 * @property context Contexto de la aplicación.
 */
class AppDataContainer (
    private val context: Context
) : AppContainer {
    /**
     * [FlightRepository] que utiliza un [OfflineFlightRepository] construido a partir de la base de datos Room.
     */
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }
}