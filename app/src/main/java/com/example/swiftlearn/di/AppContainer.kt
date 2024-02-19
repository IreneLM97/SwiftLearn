package com.example.swiftlearn.di

import android.content.Context
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.adverts.AdvertRepositoryImpl
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepositoryImpl
import com.example.swiftlearn.data.firestore.requests.RequestRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepositoryImpl
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.data.firestore.users.UserRepositoryImpl

/**
 * [AppContainer] es una interfaz que define las dependencias de la aplicación.
 */
interface AppContainer {
    val userRepository: UserRepository
    val advertRepository: AdvertRepository
    val favoriteRepository: FavoriteRepository
    val requestRepository: RequestRepository
}

/**
 * [AppDataContainer] es la implementación de [AppContainer] que proporciona instancias concretas de las dependencias de la aplicación.
 *
 * @property context Contexto de la aplicación.
 */
class AppDataContainer (
    private val context: Context
) : AppContainer {
    // Repositorio de usuarios
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }

    // Repositorio de anuncios
    override val advertRepository: AdvertRepository by lazy {
        AdvertRepositoryImpl()
    }

    // Repositorio de favoritos
    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl()
    }

    // Repositorio de solicitudes
    override val requestRepository: RequestRepository by lazy {
        RequestRepositoryImpl()
    }
}