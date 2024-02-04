package com.example.swiftlearn.model

data class User(
    val id: String,
    val username: String,
    val phone: String,
    val address: String,
    val postal: String,
    val email: String,
    val password: String,
    val rol: String
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to this.id,
            "username" to this.username,
            "phone" to this.phone,
            "address" to this.address,
            "postal" to this.postal,
            "email" to this.email,
            "rol" to this.rol
        )
    }
}
