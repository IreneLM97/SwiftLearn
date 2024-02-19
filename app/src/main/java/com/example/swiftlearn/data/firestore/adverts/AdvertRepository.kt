package com.example.swiftlearn.data.firestore.adverts

import com.example.swiftlearn.model.Advert
import kotlinx.coroutines.flow.Flow

/**
 * [AdvertRepository] es una interfaz que define las operaciones para gestionar la colección anuncios en Firestore.
 */
interface AdvertRepository {
    /**
     * Obtiene todos los anuncios como un flujo.
     */
    fun getAllAdverts(): Flow<List<Advert>>

    /**
     * Obtiene todos los anuncios asociados a un profesor como un flujo.
     *
     * @param profId ID del profesor.
     * @return Flujo de lista de anuncios.
     */
    fun getAllAdvertsByProfIdFlow(profId: String): Flow<List<Advert>>

    /**
     * Obtiene todos los anuncios asociados a un profesor como una lista
     *
     * @param profId ID del profesor.
     * @return Lista de anuncios.
     */
    suspend fun getAllAdvertsByProfId(profId: String): List<Advert>

    /**
     * Obtiene un anuncio dado su ID.
     *
     * @param advertId ID del anuncio.
     * @return Anuncio si se encuentra, de lo contrario null.
     */
    suspend fun getAdvertById(advertId: String): Advert?

    /**
     * Obtiene los IDs de los anuncios asociados a un profesor.
     *
     * @param profId ID del profesor.
     * @return Lista de IDs de anuncios.
     */
    suspend fun getAdvertsIdsByProfId(profId: String): List<String>

    /**
     * Inserta un nuevo anuncio a la colección.
     *
     * @param advert Anuncio a insertar.
     */
    suspend fun insertAdvert(advert: Advert)

    /**
     * Actualiza un anuncio dado.
     *
     * @param advert Anuncio a actualizar.
     */
    suspend fun updateAdvert(advert: Advert)

    /**
     * Elimina un anuncio dado.
     *
     * @param advert Anuncio a eliminar.
     */
    suspend fun deleteAdvert(advert: Advert)

    /**
     * Elimina todos los anuncios asociados a un profesor.
     *
     * @param profId ID del profesor.
     */
    suspend fun deleteAllAdvertsByProfId(profId: String)
}