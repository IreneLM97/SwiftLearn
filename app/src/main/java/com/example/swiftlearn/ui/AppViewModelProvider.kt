package com.example.swiftlearn.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftlearn.SwiftLearnApplication
import com.example.swiftlearn.ui.screens.student.adverts.AdvertsViewModel
import com.example.swiftlearn.ui.screens.home.HomeViewModel
import com.example.swiftlearn.ui.screens.login.LoginViewModel
import com.example.swiftlearn.ui.screens.map.MapViewModel
import com.example.swiftlearn.ui.screens.professor.myclasses.MyClassesViewModel
import com.example.swiftlearn.ui.screens.professor.editadvert.EditAdvertViewModel
import com.example.swiftlearn.ui.screens.professor.myadverts.MyAdvertsViewModel
import com.example.swiftlearn.ui.screens.professor.newadvert.NewAdvertViewModel
import com.example.swiftlearn.ui.screens.profile.ProfileViewModel
import com.example.swiftlearn.ui.screens.register.RegisterViewModel
import com.example.swiftlearn.ui.screens.student.classes.ClassesViewModel
import com.example.swiftlearn.ui.screens.student.favorites.FavoritesViewModel

/**
 * [AppViewModelProvider] es una fábrica que crea instancias de [ViewModel] para toda la aplicación.
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

        // Inicialización para MyAdvertsViewModel
        initializer {
            MyAdvertsViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
            )
        }

        // Inicialización para AdvertsViewModel
        initializer {
            AdvertsViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
            )
        }

        // Inicialización para FavoritesViewModel
        initializer {
            FavoritesViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
            )
        }

        // Inicialización para NewAdvertViewModel
        initializer {
            NewAdvertViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository
            )
        }

        // Inicialización para EditAdvertViewModel
        initializer {
            EditAdvertViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                advertRepository = swiftLearnApplication().container.advertRepository
            )
        }

        // Inicialización para MyClassesViewModel
        initializer {
            MyClassesViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
            )
        }

        // Inicialización para ClassesViewModel
        initializer {
            ClassesViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
            )
        }

        // Inicialización para MapViewModel
        initializer {
            MapViewModel(
                userRepository = swiftLearnApplication().container.userRepository
            )
        }

        // Inicialización para ProfileViewModel
        initializer {
            ProfileViewModel(
                userRepository = swiftLearnApplication().container.userRepository,
                advertRepository = swiftLearnApplication().container.advertRepository,
                favoriteRepository = swiftLearnApplication().container.favoriteRepository,
                requestRepository = swiftLearnApplication().container.requestRepository
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