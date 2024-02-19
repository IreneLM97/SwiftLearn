package com.example.swiftlearn.model

/**
 * Enumeración con todos los posibles estados de una solicitud.
 */
enum class Status {
    Aceptada,
    Rechazada,
    Pendiente
}

/**
 * [Request] es un clase que representa una solicitud.
 *
 * @property _id ID de la solicitud.
 * @property studentId ID del alumno que realizó la solicitud.
 * @property advertId ID del anuncio al que se refiere la solicitud.
 * @property status Estado de la solicitud.
 * @property date Fecha de la solicitud.
 * @property hour Hora de la solicitud.
 */
data class Request(
    val _id: String = "",
    val studentId: String = "",
    val advertId: String = "",
    val status: String = "",
    val date: String = "",
    val hour: String = ""
)