package com.example.swiftlearn.ui.screens.professor.newadvert

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level

/**
 * Clase de estado que representa el estado actual de la pantalla de nuevo anuncio.
 */
data class AdvertUiState(
    val advertDetails: AdvertDetails = AdvertDetails(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = false
)

data class AdvertDetails(
    val _id: String = "",
    val profId: String = "",
    val subject: String = "",
    val price: Int = 0,
    val classModes: MutableSet<ClassMode> = mutableSetOf(),
    val levels: MutableSet<Level> = mutableSetOf(),
    val description: String = ""
)

fun AdvertDetails.toAdvert(): Advert = Advert(
    _id = _id,
    profId = profId,
    subject = subject,
    price = price,
    classModes = classModes.joinToString(", "),
    levels = levels.joinToString(", "),
    description = description
)

fun Advert.toAdvertDetails(): AdvertDetails {
    val classModesSet = classModes.split(", ").mapNotNull { str ->
        when (str) {
            "Presencial" -> ClassMode.Presencial
            "Online" -> ClassMode.Online
            "Hibrido" -> ClassMode.Hibrido
            else -> null
        }
    }.toMutableSet()

    val levelsSet = levels.split(", ").mapNotNull { str ->
        when (str) {
            "Primaria" -> Level.Primaria
            "ESO" -> Level.ESO
            "Bachillerato" -> Level.Bachillerato
            "FP" -> Level.FP
            "Universidad" -> Level.Universidad
            "Adultos" -> Level.Adultos
            else -> null
        }
    }.toMutableSet()

    return AdvertDetails(
        _id = _id,
        profId = profId,
        subject = subject,
        price = price,
        classModes = classModesSet,
        levels = levelsSet,
        description = description
    )
}