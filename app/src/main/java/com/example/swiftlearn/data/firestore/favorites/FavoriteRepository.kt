package com.example.swiftlearn.data.firestore.favorites

import com.example.swiftlearn.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun getFavoriteByInfo(userId: String, advertId: String): Favorite?
    fun getAllFavoritesByUser(userId: String): Flow<List<Favorite>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
    suspend fun deleteAllFavoritesByUserId(userId: String)
    suspend fun deleteAllFavoritesByAdvertId(advertId: String)
    suspend fun deleteAllFavoritesByAdvertIds(advertsIds: List<String>)
}