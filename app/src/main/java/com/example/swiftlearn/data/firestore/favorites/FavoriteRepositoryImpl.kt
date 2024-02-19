package com.example.swiftlearn.data.firestore.favorites

import com.example.swiftlearn.model.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * [FavoriteRepositoryImpl] es la implementación de [FavoriteRepository] que permite gestionar la colección favoritos en Firestore.
 */
class FavoriteRepositoryImpl: FavoriteRepository {
    // Obtenemos una instancia de FirebaseFirestore
    private val firestore = FirebaseFirestore.getInstance()
    // Obtenemos la colección de favoritos
    private val favoritesCollection = firestore.collection("favorites")

    override suspend fun getFavoriteByInfo(studentId: String, advertId: String): Favorite? {
        return try {
            // Realizamos la consulta a la colección filtrando por studentId y advertId
            val query = favoritesCollection
                .whereEqualTo("studentId", studentId) // Aplicamos el filtro de studentId
                .whereEqualTo("advertId", advertId) // Aplicamos el filtro de advertId
                .get()
                .await()
            // Convertimos el resultado obtenido a la clase Favorite
            query.documents.firstOrNull()?.toObject(Favorite::class.java)
        } catch (e: Exception) {
            // En caso de error, devolvemos null
            null
        }
    }

    override fun getAllFavoritesByStudentId(studentId: String): Flow<List<Favorite>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de favoritos
        val subscription = favoritesCollection
            .whereEqualTo("studentId", studentId) // Aplicamos el filtro de studentId
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar los favoritos
                val favoritesList = mutableListOf<Favorite>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val favorite = documentSnapshot.toObject(Favorite::class.java)
                    favorite?.let { favoritesList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(favoritesList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        try {
            // Agregamos a la colección el nuevo favorito
            val document = favoritesCollection.add(favorite).await()
            // Actualizamos el favorito copiando el ID asignado por Firestore al insertarlo
            val favoriteWithId = favorite.copy(_id = document.id)
            favoritesCollection.document(document.id).set(favoriteWithId)
        } catch (_: Exception) {}
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        try {
            favoritesCollection.document(favorite._id).delete().await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAllFavoritesByStudentId(studentId: String) {
        try {
            // Realizamos la consulta a la colección filtrando por studentId
            val query = favoritesCollection.whereEqualTo("studentId", studentId).get().await()

            // Inicializamos un nuevo lote
            val batch = firestore.batch()
            // Agregamos todas las operaciones de eliminación al lote
            query.documents.forEach { document ->
                batch.delete(document.reference)
            }

            // Commit del lote para ejecutar las operaciones de eliminación
            batch.commit().await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAllFavoritesByAdvertId(advertId: String) {
        try {
            // Realizamos la consulta a la colección filtrando por advertId
            val query = favoritesCollection.whereEqualTo("advertId", advertId).get().await()

            // Inicializamos un nuevo lote
            val batch = firestore.batch()
            // Agregamos todas las operaciones de eliminación al lote
            query.documents.forEach { document ->
                batch.delete(document.reference)
            }

            // Commit del lote para ejecutar las operaciones de eliminación
            batch.commit().await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAllFavoritesByAdvertIds(advertsIds: List<String>) {
        // Recorremos la lista de advertsIds
        for (advertId in advertsIds) {
            // Eliminamos los favoritos asociados a ese anuncio
            deleteAllFavoritesByAdvertId(advertId)
        }
    }
}