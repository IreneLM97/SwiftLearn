package com.example.swiftlearn.data.firestore.adverts

import com.example.swiftlearn.model.Advert
import kotlinx.coroutines.flow.Flow

interface AdvertRepository {
    fun getAllAdverts(): Flow<List<Advert>>
    fun getAllAdvertsByProfIdFlow(profId: String): Flow<List<Advert>>
    suspend fun getAllAdvertsByProfId(profId: String): List<Advert>
    suspend fun getAdvertById(advertId: String): Advert?
    suspend fun getAdvertsIdsByProfId(profId: String): List<String>
    suspend fun insertAdvert(advert: Advert)
    suspend fun updateAdvert(advert: Advert)
    suspend fun deleteAdvert(advert: Advert)
    suspend fun deleteAllAdvertsByProfId(profId: String)
}