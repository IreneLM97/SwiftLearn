package com.example.swiftlearn.data.firestore.adverts

import com.example.swiftlearn.model.Advert
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AdvertRepositoryImpl: AdvertRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val advertsCollection = firestore.collection("adverts")

    override fun getAllAdverts(): Flow<List<Advert>> = callbackFlow {
        val subscription = advertsCollection
            .addSnapshotListener { querySnapshot, _ ->
                val advertList = mutableListOf<Advert>()
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val advert = documentSnapshot.toObject(Advert::class.java)
                    advert?.let { advertList.add(it) }
                }
                trySend(advertList).isSuccess
            }
        awaitClose {
            subscription.remove()
        }
    }

    override fun getAllAdvertsByProfIdFlow(profId: String): Flow<List<Advert>> = callbackFlow {
        val subscription = advertsCollection
            .whereEqualTo("profId", profId)
            .addSnapshotListener { querySnapshot, _ ->
                val advertsList = mutableListOf<Advert>()
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val advert = documentSnapshot.toObject(Advert::class.java)
                    advert?.let { advertsList.add(it) }
                }
                trySend(advertsList).isSuccess
            }
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun getAllAdvertsByProfId(profId: String): List<Advert> {
        val query = advertsCollection.whereEqualTo("profId", profId).get().await()

        val advertsList = mutableListOf<Advert>()
        query.documents.forEach { document ->
            val advert = document.toObject(Advert::class.java)
            advert?.let { advertsList.add(it) }
        }

        return advertsList
    }

    override suspend fun getAdvertById(advertId: String): Advert? {
        val document = advertsCollection.document(advertId).get().await()
        return document.toObject(Advert::class.java)
    }

    override suspend fun getAdvertsIdsByProfId(profId: String): List<String> {
        return try {
            val query = advertsCollection.whereEqualTo("profId", profId).get().await()
            query.documents.mapNotNull { document ->
                document.id
            }
        } catch (e: Exception) {
            emptyList() // Manejar el caso de error devolviendo una lista vacía
        }
    }

    override suspend fun insertAdvert(advert: Advert) {
        try {
            val document = advertsCollection.add(advert).await()
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
            val query = advertsCollection.whereEqualTo("profId", profId).get().await()
            query.documents.forEach { advertsCollection.document(it.id).delete() }
        } catch (_: Exception) {}
    }
}