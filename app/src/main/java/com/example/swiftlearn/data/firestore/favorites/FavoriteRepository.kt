package com.example.swiftlearn.data.firestore.favorites

import com.example.swiftlearn.model.Favorite
import kotlinx.coroutines.flow.Flow

/**
 * [FavoriteRepository] es una interfaz que define las operaciones para gestionar la colección favoritos en Firestore.
 */
interface FavoriteRepository {
    /**
     * Obtiene un favorito a partir del ID del alumno y del ID del anuncio.
     *
     * @param studentId ID del alumno que marcó como favorito el anuncio.
     * @param advertId ID del anuncio marcado como favorito.
     * @return Favorito si se encuentra, de lo contrario null.
     */
    suspend fun getFavoriteByInfo(studentId: String, advertId: String): Favorite?

    /**
     * Obtiene todos los favoritos de un alumno como un flujo.
     *
     * @param studentId ID del alumno.
     * @return Flujo de lista de favoritos.
     */
    fun getAllFavoritesByStudentId(studentId: String): Flow<List<Favorite>>

    /**
     * Inserta un nuevo favorito a la colección.
     *
     * @param favorite Favorito a insertar.
     */
    suspend fun insertFavorite(favorite: Favorite)

    /**
     * Elimina un favorito dado.
     *
     * @param favorite Favorito a eliminar.
     */
    suspend fun deleteFavorite(favorite: Favorite)

    /**
     * Elimina todos los favoritos asociados a un alumno.
     *
     * @param studentId ID del alumno.
     */
    suspend fun deleteAllFavoritesByStudentId(studentId: String)

    /**
     * Elimina todos los favoritos asociados a un anuncio.
     *
     * @param advertId ID del anuncio.
     */
    suspend fun deleteAllFavoritesByAdvertId(advertId: String)

    /**
     * Elimina todos los favoritos asociados a una lista de IDs de anuncios.
     *
     * @param advertsIds Lista de IDs de anuncios.
     */
    suspend fun deleteAllFavoritesByAdvertIds(advertsIds: List<String>)
}