package com.example.swiftlearn.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftlearn.SwiftLearnApplication
import com.example.swiftlearn.ui.screens.login.LoginViewModel
import com.example.swiftlearn.ui.screens.register.RegisterViewModel

/**
 * Fábrica que crea instancias de ViewModel para toda la aplicación.
 */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        // Inicialización para LoginViewModel
        initializer {
            LoginViewModel(
                userPreferencesRepository = swiftLearnApplication().userPreferencesRepository
            )
        }

        // Inicialización para RegisterViewModel
        initializer {
            // Crear e inicializar la instancia de RegisterViewModel
            RegisterViewModel(
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