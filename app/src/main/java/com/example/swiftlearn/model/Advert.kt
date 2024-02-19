package com.example.swiftlearn.model

/**
 * Enumeración con todos los modos de clase posibles.
 */
enum class ClassMode {
    Presencial,
    Online,
    Hibrido
}

/**
 * Enumeración con todos los niveles educativos posibles.
 */
enum class Level {
    Primaria,
    ESO,
    Bachillerato,
    FP,
    Universidad,
    Adultos
}

/**
 * [Advert] es una clase que representa un anuncio.
 *
 * @property _id ID del anuncio.
 * @property profId ID del profesor que publicó el anuncio.
 * @property subject Asignatura del anuncio.
 * @property price Precio del servicio ofrecido en el anuncio.
 * @property classModes Modos de clase ofrecidos en el anuncio (separados por coma si son múltiples).
 * @property levels Niveles educativos a los que se dirige el anuncio (separados por coma si son múltiples).
 * @property description Descripción del servicio ofrecido en el anuncio.
 */
data class Advert(
    val _id: String = "",
    val profId: String = "",
    val subject: String = "",
    val price: Int = 0,
    val classModes: String = "",
    val levels: String = "",
    val description: String = ""
)
