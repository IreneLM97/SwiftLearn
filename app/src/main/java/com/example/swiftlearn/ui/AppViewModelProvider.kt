package com.example.swiftlearn.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftlearn.SwiftLearnApplication
import com.example.swiftlearn.ui.screens.adverts.AdvertsListViewModel
import com.example.swiftlearn.ui.screens.home.HomeViewModel
import com.example.swiftlearn.ui.screens.login.LoginViewModel
import com.example.swiftlearn.ui.screens.newadvert.NewAdvertViewModel
import com.example.swiftlearn.ui.screens.profile.ProfileViewModel
import com.example.swiftlearn.ui.screens.register.RegisterViewModel

/**
 * Fábrica que crea instancias de ViewModel para toda la aplicación.
 */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        // Inicialización para LoginViewModel
        initializer {
            LoginViewModel(
                userPreferencesRepository = swiftLearnApplication().userPreferencesRepository,
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para RegisterViewModel
        initializer {
            // Crear e inicializar la instancia de RegisterViewModel
            RegisterViewModel(
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para HomeViewModel
        initializer {
            // Crear e inicializar la instancia de HomeViewModel
            HomeViewModel(
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para NewAdvertViewModel
        initializer {
            // Crear e inicializar la instancia de AdvertsListViewModel
            NewAdvertViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository
            )
        }

        // Inicialización para AdvertsListViewModel
        initializer {
            // Crear e inicializar la instancia de AdvertsListViewModel
            AdvertsListViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository
            )
        }

        // Inicialización para ProfileViewModel
        initializer {
            // Crear e inicializar la instancia de ProfileViewModel
            ProfileViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository
            )
        }
    }
}

/**
 * Función de extensión para consultar el objeto [Application]
 * y devolver una instancia de [SwiftLearnApplication].
 */
fun CreationExtras.swiftLearnApplication(): SwiftLearnApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SwiftLearnApplication)