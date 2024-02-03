package com.example.swiftlearn.di

import android.content.Context

/**
 * Interfaz que define las dependencias de la aplicación.
 */
interface AppContainer {

}

/**
 * Implementación de [AppContainer] que proporciona instancias concretas de las dependencias de la aplicación.
 *
 * @property context Contexto de la aplicación.
 */
class AppDataContainer (
    private val context: Context
) : AppContainer {

}