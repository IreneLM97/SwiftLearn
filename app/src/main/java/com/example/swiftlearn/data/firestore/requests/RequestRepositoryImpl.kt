package com.example.swiftlearn.data.firestore.requests

import com.example.swiftlearn.model.Request
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RequestRepositoryImpl: RequestRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val requestsCollection = firestore.collection("classes")

    override fun getAllRequestsByUserId(userId: String): Flow<List<Request>> = callbackFlow {
        val subscription = requestsCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { querySnapshot, _ ->
                val requestsList = mutableListOf<Request>()
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val request = documentSnapshot.toObject(Request::class.java)
                    request?.let { requestsList.add(it) }
                }
                trySend(requestsList).isSuccess
            }
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun insertRequest(request: Request) {
        try {
            val document = requestsCollection.add(request).await()
            val requestWithId = request.copy(_id = document.id)
            requestsCollection.document(document.id).set(requestWithId)
        } catch (_: Exception) {}
    }

    override suspend fun updateRequest(request: Request) {
        try {
            requestsCollection.document(request._id).set(request).await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteRequest(request: Request) {
        try {
            requestsCollection.document(request._id).delete().await()
        } catch (_: Exception) {}
    }
}