package com.example.swiftlearn.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftlearn.SwiftLearnApplication
import com.example.swiftlearn.ui.screens.student.adverts.AdvertsListViewModel
import com.example.swiftlearn.ui.screens.home.HomeViewModel
import com.example.swiftlearn.ui.screens.login.LoginViewModel
import com.example.swiftlearn.ui.screens.professor.newadvert.NewAdvertViewModel
import com.example.swiftlearn.ui.screens.profile.ProfileViewModel
import com.example.swiftlearn.ui.screens.register.RegisterViewModel
import com.example.swiftlearn.ui.screens.student.SessionViewModel
import com.example.swiftlearn.ui.screens.student.favorites.FavoritesListViewModel

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
            RegisterViewModel(
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para HomeViewModel
        initializer {
            HomeViewModel(
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para SessionViewModel
        initializer {
            SessionViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository
            )
        }

        // Inicialización para AdvertsListViewModel
        initializer {
            AdvertsListViewModel(
                sessionViewModel = swiftLearnApplication().container.sessionViewModel,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository
            )
        }

        // Inicialización para FavoritesListViewModel
        initializer {
            FavoritesListViewModel(
                sessionViewModel = swiftLearnApplication().container.sessionViewModel,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository
            )
        }

        // Inicialización para NewAdvertViewModel
        initializer {
            NewAdvertViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository
            )
        }

        // Inicialización para ProfileViewModel
        initializer {
            ProfileViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository
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