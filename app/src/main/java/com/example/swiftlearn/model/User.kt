package com.example.swiftlearn.model

/**
 * Enumeración con todos los posibles roles de usuario.
 */
enum class Role {
    Profesor,
    Alumno,
    None
}

/**
 * Clase que representa a un usuario.
 *
 * @property _id ID del usuario.
 * @property authId ID de autenticación en Firebase del usuario.
 * @property username Nombre del usuario.
 * @property phone Número de teléfono del usuario.
 * @property address Dirección del usuario.
 * @property postal Código postal del usuario.
 * @property latitude Latitud de la ubicación del usuario.
 * @property longitude Longitud de la ubicación del usuario.
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 * @property role Rol del usuario.
 */
data class User(
    val _id: String = "",
    val authId: String = "",
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val email: String = "",
    val password: String = "",
    val role: Role = Role.None
) {
    /**
     * Convierte la instancia de usuario a un mapa clave-valor.
     *
     * @return Mapa mutable con los atributos del usuario que se desean guardar en la colección.
     */
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "_id" to this._id,
            "authId" to this.authId,
            "username" to this.username,
            "phone" to this.phone,
            "address" to this.address,
            "postal" to this.postal,
            "latitude" to this.latitude,
            "longitude" to this.longitude,
            "email" to this.email,
            "role" to this.role
        )
    }
}
