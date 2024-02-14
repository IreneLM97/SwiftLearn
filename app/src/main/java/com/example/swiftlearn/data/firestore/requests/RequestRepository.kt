package com.example.swiftlearn.data.firestore.requests

import com.example.swiftlearn.model.Request
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    fun getAllRequestsByStudentId(userId: String): Flow<List<Request>>
    fun getAllRequestsByAdvertsId(advertIds: List<String>): Flow<List<Request>>
    suspend fun insertRequest(request: Request)
    suspend fun updateRequest(request: Request)
    suspend fun deleteRequest(request: Request)
}