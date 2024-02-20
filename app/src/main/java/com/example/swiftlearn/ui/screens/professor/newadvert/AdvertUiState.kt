package com.example.swiftlearn.ui.screens.professor.newadvert

import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level

/**
 * Estado de la interfaz de usuario para las pantallas de nuevo anuncio y editar anuncio.
 *
 * @property advertDetails Detalles del anuncio.
 * @property isEntryValid Indica si el formulario es válido.
 * @property isLoading Indica si la pantalla está en estado de carga.
 */
data class AdvertUiState(
    val advertDetails: AdvertDetails = AdvertDetails(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = false
)

/**
 * Clase que representa los detalles de un anuncio.
 *
 * @property _id ID del anuncio.
 * @property profId ID del profesor asociado al anuncio.
 * @property subject Asignatura del anuncio.
 * @property price Precio del anuncio.
 * @property classModes Modos de clase del anuncio.
 * @property levels Niveles educativos del anuncio.
 * @property description Descripción del anuncio.
 */
data class AdvertDetails(
    val _id: String = "",
    val profId: String = "",
    val subject: String = "",
    val price: Int = 0,
    val classModes: MutableSet<ClassMode> = mutableSetOf(),
    val levels: MutableSet<Level> = mutableSetOf(),
    val description: String = ""
)

/**
 * Función que convierte los detalles de un anuncio a un objeto Advert.
 *
 * @return Objeto Advert generado a partir de los detalles del anuncio.
 */
fun AdvertDetails.toAdvert(): Advert = Advert(
    _id = _id,
    profId = profId,
    subject = subject,
    price = price,
    classModes = classModes.joinToString(", "),
    levels = levels.joinToString(", "),
    description = description
)

/**
 * Función que convierte un objeto Advert en sus detalles correspondientes.
 *
 * @return Detalles del anuncio generados a partir del objeto Advert.
 */
fun Advert.toAdvertDetails(): AdvertDetails {
    // Obtenemos conjunto de ClassModes a partir del atributo del objeto
    val classModesSet = classModes.split(", ").mapNotNull { str ->
        when (str) {
            "Presencial" -> ClassMode.Presencial
            "Online" -> ClassMode.Online
            "Hibrido" -> ClassMode.Hibrido
            else -> null
        }
    }.toMutableSet()

    // Obtenemos conjunto de Levels a partir del atributo del objeto
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