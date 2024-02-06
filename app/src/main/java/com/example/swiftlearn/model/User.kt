package com.example.swiftlearn.model

enum class Rol {
    Profesor,
    Alumno
}

data class User(
    val authId: String = "",
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = "",
    val email: String = "",
    val password: String = "",
    val rol: Rol = Rol.Alumno
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "authId" to this.authId,
            "username" to this.username,
            "phone" to this.phone,
            "address" to this.address,
            "postal" to this.postal,
            "email" to this.email,
            "rol" to this.rol
        )
    }
}
