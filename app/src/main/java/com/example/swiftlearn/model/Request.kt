package com.example.swiftlearn.model

enum class Status {
    Aceptada,
    Rechazada,
    Pendiente
}

data class Request(
    val _id: String = "",
    val studentId: String = "",
    val advertId: String = "",
    val status: String = "",
    val date: String = "",
    val hour: String = ""
)