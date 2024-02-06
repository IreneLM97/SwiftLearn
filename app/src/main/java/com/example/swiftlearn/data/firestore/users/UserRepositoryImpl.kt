package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override fun getUserById(userId: String): Flow<User?> {
        return flow {
            val documentSnapshot = usersCollection.document(userId).get().await()
            emit(documentSnapshot.toObject(User::class.java))
        }
    }

    override suspend fun insertUser(user: User) {
        usersCollection.add(user.toMap())
    }

    override suspend fun deleteUser(user: User) {
        usersCollection.document(user.id)
            .delete()
            .await()
    }

    override suspend fun updateUser(user: User) {
        usersCollection.document(user.id)
            .set(user.toMap())
            .await()
    }
}