package com.example.swiftlearn.model

enum class Role {
    Profesor,
    Alumno,
    None
}

data class User(
    val _id: String = "",
    val authId: String = "",
    val username: String = "",
    val phone: String = "",
    val address: String = "",
    val postal: String = "",
    val email: String = "",
    val password: String = "",
    val role: Role = Role.None
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "_id" to this._id,
            "authId" to this.authId,
            "username" to this.username,
            "phone" to this.phone,
            "address" to this.address,
            "postal" to this.postal,
            "email" to this.email,
            "role" to this.role
        )
    }
}
