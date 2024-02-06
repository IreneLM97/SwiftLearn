package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.User

interface UserRepository {
    suspend fun getUserByAuthId(authId: String): User?
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}