package com.example.swiftlearn.data.firestore.requests

import com.example.swiftlearn.model.Request
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones para gestionar solicitudes de clases en Firestore.
 */
interface RequestRepository {
    /**
     * Obtiene todas las solicitudes asociadas a un alumno como un flujo.
     *
     * @param studentId ID del alumno.
     * @return Flujo de lista de solicitudes.
     */
    fun getAllRequestsByStudentId(studentId: String): Flow<List<Request>>

    /**
     * Obtiene todas las solicitudes asociadas a una lista de IDs de anuncios.
     *
     * @param advertsIds Lista de IDs de anuncios.
     * @return Flujo de lista de solicitudes.
     */
    fun getAllRequestsByAdvertIds(advertsIds: List<String>): Flow<List<Request>>

    /**
     * Inserta una nueva solicitud a la colección.
     *
     * @param request Solicitud a insertar.
     */
    suspend fun insertRequest(request: Request)

    /**
     * Actualiza una solicitud dada.
     *
     * @param request Solicitud a actualizar.
     */
    suspend fun updateRequest(request: Request)

    /**
     * Elimina una solicitud dada.
     *
     * @param request Solicitud a eliminar.
     */
    suspend fun deleteRequest(request: Request)
}