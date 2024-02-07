package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    /*override fun getUserById(userId: String): Flow<User?> {
        return flow {
            val documentSnapshot = usersCollection.document(userId).get().await()
            emit(documentSnapshot.toObject(User::class.java))
        }
    }*/

    override suspend fun getUserByAuthId(authId: String): User? {
        return try {
            val query = usersCollection.whereEqualTo("authId", authId).get().await()
            val document = query.documents.firstOrNull()
            document?.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertUser(user: User) {
        usersCollection.add(user.toMap())
    }

    override suspend fun deleteUser(user: User) {
        try {
            val query = usersCollection.whereEqualTo("authId", user.authId).get().await()
            val document = query.documents.firstOrNull()
            document?.reference?.delete()
        } catch (_: Exception) {}
    }

    override suspend fun updateUser(user: User) {
        try {
            val query = usersCollection.whereEqualTo("authId", user.authId).get().await()
            val document = query.documents.firstOrNull()
            document?.reference?.set(user.toMap())
        } catch (_: Exception) {}
    }
}