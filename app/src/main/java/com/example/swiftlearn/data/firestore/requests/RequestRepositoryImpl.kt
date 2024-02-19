package com.example.swiftlearn.data.firestore.requests

import com.example.swiftlearn.model.Request
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Implementación de [RequestRepository] que permite gestionar la colección solicitudes en Firestore.
 */
class RequestRepositoryImpl: RequestRepository {
    // Obtenemos una instancia de FirebaseFirestore
    private val firestore = FirebaseFirestore.getInstance()
    // Obtenemos la colección de solicitudes
    private val requestsCollection = firestore.collection("requests")

    override fun getAllRequestsByStudentId(studentId: String): Flow<List<Request>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de solicitudes
        val subscription = requestsCollection
            .whereEqualTo("studentId", studentId) // Aplicamos el filtro de studentId
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar las solicitudes
                val requestsList = mutableListOf<Request>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val request = documentSnapshot.toObject(Request::class.java)
                    request?.let { requestsList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(requestsList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override fun getAllRequestsByAdvertIds(advertsIds: List<String>): Flow<List<Request>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de solicitudes
        val subscription = requestsCollection
            .whereIn("advertId", advertsIds) // Aplicamos el filtro de advertId en advertsIds
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar las solicitudes
                val requestsList = mutableListOf<Request>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val request = documentSnapshot.toObject(Request::class.java)
                    request?.let { requestsList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(requestsList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun insertRequest(request: Request) {
        try {
            // Agregamos a la colección la nueva solicitud
            val document = requestsCollection.add(request).await()
            // Actualizamos la solicitud copiando el ID asignado por Firestore al insertarlo
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

    override suspend fun deleteAllRequestsByStudentId(studentId: String) {
        try {
            // Realizamos la consulta a la colección filtrando por studentId
            val query = requestsCollection.whereEqualTo("studentId", studentId).get().await()

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

    override suspend fun deleteAllRequestsByAdvertId(advertId: String) {
        try {
            // Realizamos la consulta a la colección filtrando por advertId
            val query = requestsCollection.whereEqualTo("advertId", advertId).get().await()

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

    override suspend fun deleteAllRequestsByAdvertIds(advertsIds: List<String>) {
        // Recorremos la lista de advertsIds
        for (advertId in advertsIds) {
            // Eliminamos las solicitudes asociados a ese anuncio
            deleteAllRequestsByAdvertId(advertId)
        }
    }
}