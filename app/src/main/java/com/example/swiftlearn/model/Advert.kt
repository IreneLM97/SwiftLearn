package com.example.swiftlearn.model

enum class ClassMode {
    Presencial,
    Online,
    Hibrido
}

enum class Level {
    Primaria,
    ESO,
    Bachillerato,
    FP,
    Universidad,
    Adultos
}

data class Advert(
    val _id: String = "",
    val profId: String = "",
    val subject: String = "",
    val price: Int = 0,
    val classModes: String = "",
    val levels: String = "",
    val description: String = ""
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "_id" to this._id,
            "profId" to this.profId,
            "subject" to this.subject,
            "price" to this.price,
            "classModes" to this.classModes,
            "levels" to this.levels,
            "description" to this.description
        )
    }
}
