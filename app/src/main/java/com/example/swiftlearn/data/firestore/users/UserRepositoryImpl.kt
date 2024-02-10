package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override suspend fun getUserByAuthId(authId: String): User? {
        return try {
            val query = usersCollection.whereEqualTo("authId", authId).get().await()
            query.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertUser(user: User) {
        try {
            val document = usersCollection.add(user.toMap()).await()
            val userWithId = user.copy(_id = document.id)
            usersCollection.document(document.id).set(userWithId.toMap())
        } catch (_: Exception) {}
    }

    override suspend fun updateUser(user: User) {
        try {
            usersCollection.document(user._id).set(user.toMap()).await()
        } catch (_: Exception) {}
    }

    override suspend fun deleteUser(user: User) {
        try {
            usersCollection.document(user._id).delete().await()
        } catch (_: Exception) {}
    }
}