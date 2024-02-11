package com.example.swiftlearn.ui.screens.newadvert

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level

/**
 * Clase de estado que representa el estado actual de la pantalla de nuevo anuncio.
 */
data class NewAdvertUiState(
    val newAdvertDetails: NewAdvertDetails = NewAdvertDetails(),
    val isEntryValid: Boolean = false,
    val loadingState: Boolean = false
)

data class NewAdvertDetails(
    val subject: String = "",
    val price: Int = 0,
    val classModes: MutableSet<ClassMode> = mutableSetOf(),
    val levels: MutableSet<Level> = mutableSetOf(),
    val description: String = ""
)

fun NewAdvertDetails.toAdvert(): Advert = Advert(
    subject = subject,
    price = price,
    classModes = classModes.joinToString(", "),
    levels = levels.joinToString(", "),
    description = description
)