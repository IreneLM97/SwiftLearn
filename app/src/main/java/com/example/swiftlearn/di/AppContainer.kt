package com.example.swiftlearn.di

import android.content.Context
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.adverts.AdvertRepositoryImpl
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepositoryImpl
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.data.firestore.users.UserRepositoryImpl
import com.example.swiftlearn.ui.screens.student.SessionViewModel

/**
 * Interfaz que define las dependencias de la aplicación.
 */
interface AppContainer {
    val userRepository: UserRepository
    val advertRepository: AdvertRepository
    val favoriteRepository: FavoriteRepository
    val sessionViewModel: SessionViewModel
}

/**
 * Implementación de [AppContainer] que proporciona instancias concretas de las dependencias de la aplicación.
 *
 * @property context Contexto de la aplicación.
 */
class AppDataContainer (
    private val context: Context
) : AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }

    override val advertRepository: AdvertRepository by lazy {
        AdvertRepositoryImpl()
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl()
    }

    override val sessionViewModel: SessionViewModel by lazy {
        SessionViewModel(
            userRepository = userRepository,
            advertRepository = advertRepository,
            favoriteRepository = favoriteRepository
        )
    }
}