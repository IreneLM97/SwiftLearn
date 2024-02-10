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
    val classModes: Set<ClassMode> = emptySet(),
    val levels: Set<Level> = emptySet(),
    val description: String = ""
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "_id" to this._id,
            "profId" to this.profId,
            "subject" to this.subject,
            "price" to this.price,
            "classModes" to this.classModes.joinToString(","),
            "levels" to this.levels.joinToString(","),
            "description" to this.description
        )
    }
}
