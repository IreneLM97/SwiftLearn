package com.example.swiftlearn.data.firestore.adverts

import com.example.swiftlearn.model.Advert
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * [AdvertRepositoryImpl] es la implementación de [AdvertRepository] que permite gestionar la colección anuncios en Firestore.
 */
class AdvertRepositoryImpl: AdvertRepository {
    // Obtenemos una instancia de FirebaseFirestore
    private val firestore = FirebaseFirestore.getInstance()
    // Obtenemos la colección de anuncios
    private val advertsCollection = firestore.collection("adverts")

    override fun getAllAdverts(): Flow<List<Advert>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de anuncios
        val subscription = advertsCollection
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar los anuncios
                val advertList = mutableListOf<Advert>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val advert = documentSnapshot.toObject(Advert::class.java)
                    advert?.let { advertList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(advertList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override fun getAllAdvertsByProfIdFlow(profId: String): Flow<List<Advert>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de anuncios
        val subscription = advertsCollection
            .whereEqualTo("profId", profId)  // Aplicamos el filtro de profId
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar los anuncios
                val advertsList = mutableListOf<Advert>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val advert = documentSnapshot.toObject(Advert::class.java)
                    advert?.let { advertsList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(advertsList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun getAllAdvertsByProfId(profId: String): List<Advert> {
        // Realizamos la consulta a la colección filtrando por profId
        val query = advertsCollection.whereEqualTo("profId", profId).get().await()

        // Inicializamos lista mutable para almacenar los anuncios
        val advertsList = mutableListOf<Advert>()
        // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
        query.documents.forEach { document ->
            val advert = document.toObject(Advert::class.java)
            advert?.let { advertsList.add(it) }
        }

        return advertsList
    }

    override suspend fun getAdvertById(advertId: String): Advert? {
        // Obtenemos el documento correspondiente al advertId dado
        val document = advertsCollection.document(advertId).get().await()
        // Convertimos el resultado obtenido a la clase Advert
        return document.toObject(Advert::class.java)
    }

    override suspend fun getAdvertsIdsByProfId(profId: String): List<String> {
        return try {
            // Realizamos la consulta a la colección filtrando por profId
            val query = advertsCollection.whereEqualTo("profId", profId).get().await()
            // Mapeamos el resultado para devolver los Ids de los anuncios encontrados
            query.documents.mapNotNull { document -> document.id }
        } catch (e: Exception) {
            // Si ocurre algún error devolvemos lista vacía
            emptyList()
        }
    }

    override suspend fun insertAdvert(advert: Advert) {
        try {
            // Agregamos a la colección el nuevo anuncio
            val document = advertsCollection.add(advert).await()
            // Actualizamos el anuncio copiando el ID asignado por Firestore al insertarlo
            val advertWithId = advert.copy(_id = document.id)
            advertsCollection.document(document.id).set(advertWithId)
        } catch (_: Exception) {}
    }

    override suspend fun updateAdvert(advert: Advert) {
        try {
            advertsCollection.document(advert._id).set(advert).await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAdvert(advert: Advert) {
        try {
            advertsCollection.document(advert._id).delete().await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteAllAdvertsByProfId(profId: String) {
        try {
            // Realizamos la consulta a la colección filtrando por profId
            val query = advertsCollection.whereEqualTo("profId", profId).get().await()

            // Inicializamos un nuevo lote
            val batch = firestore.batch()
            // Agregamos todas las operaciones de eliminación al lote
            query.documents.forEach { document ->
                batch.delete(advertsCollection.document(document.id))
            }

            // Commit del lote para ejecutar las operaciones de eliminación
            batch.commit().await()
        } catch (_: Exception) {}
    }
}