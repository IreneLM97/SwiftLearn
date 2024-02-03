package com.example.swiftlearn.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftlearn.SwiftLearnApplication
import com.example.swiftlearn.ui.screens.login.LoginViewModel

object AppViewModelProvider {
    /**
     * Fábrica que crea instancias de [SearchViewModel].
     */
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            // Obtener la instancia de la aplicación
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SwiftLearnApplication)

            // Obtener los repositorios necesarios desde el contenedor de la aplicación
            val preferencesRepository = application.userPreferencesRepository

            // Crear e inicializar la instancia de SearchViewModel con los repositorios
            LoginViewModel(
                userPreferencesRepository = preferencesRepository
            )
        }
    }
}