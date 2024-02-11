package com.example.swiftlearn.data.firestore.favorites

import com.example.swiftlearn.model.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FavoriteRepositoryImpl: FavoriteRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val favoritesCollection = firestore.collection("favorites")

    override suspend fun getFavoriteByInfo(userId: String, advertId: String): Favorite? {
        return try {
            val query = favoritesCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("advertId", advertId)
                .get()
                .await()
            query.documents.firstOrNull()?.toObject(Favorite::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllFavoritesByUser(userId: String): Flow<List<Favorite>> = callbackFlow {
        val subscription = favoritesCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { querySnapshot, _ ->
                val favoritesList = mutableListOf<Favorite>()
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val favorite = documentSnapshot.toObject(Favorite::class.java)
                    favorite?.let { favoritesList.add(it) }
                }
                trySend(favoritesList).isSuccess
            }
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        try {
            val document = favoritesCollection.add(favorite).await()
            val favoriteWithId = favorite.copy(_id = document.id)
            favoritesCollection.document(document.id).set(favoriteWithId)
        } catch (_: Exception) {}
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        try {
            favoritesCollection.document(favorite._id).delete().await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAllFavoritesByUserId(userId: String) {
        try {
            val query = favoritesCollection.whereEqualTo("userId", userId).get().await()
            val batch = firestore.batch()
            query.documents.forEach { document ->
                batch.delete(document.reference)
            }
            batch.commit().await()
        } catch (_: Exception) {}
    }
}