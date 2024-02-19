package com.example.swiftlearn.model

/**
 * [Favorite] es una clase que representa un favorito.
 *
 * @property _id ID del favorito.
 * @property studentId ID del alumno que marcó como favorito el anuncio.
 * @property advertId ID del anuncio marcado como favorito.
 */
data class Favorite(
    val _id: String = "",
    val studentId: String = "",
    val advertId: String = ""
)