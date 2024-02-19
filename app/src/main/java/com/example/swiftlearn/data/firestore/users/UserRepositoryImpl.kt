package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.Role
import com.example.swiftlearn.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Implementación de [UserRepository] que permite gestionar la colección usuarios en Firestore.
 */
class UserRepositoryImpl: UserRepository {
    // Obtenemos una instancia de FirebaseFirestore
    private val firestore = FirebaseFirestore.getInstance()
    // Obtenemos la colección de usuarios
    private val usersCollection = firestore.collection("users")

    override suspend fun getUserByAuthId(authId: String): User? {
        return try {
            // Realizamos la consulta a la colección filtrando por authId
            val query = usersCollection.whereEqualTo("authId", authId).get().await()
            // Convertimos el resultado obtenido a la clase User
            query.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            // En caso de error, devolvemos null
            null
        }
    }

    override fun getAllProfessors(): Flow<List<User>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de usuarios
        val subscription = usersCollection
            .whereEqualTo("role", Role.Profesor) // Aplicamos el filtro de role Profesor
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar los usuarios
                val professorsList = mutableListOf<User>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val professor = documentSnapshot.toObject(User::class.java)
                    professor?.let { professorsList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(professorsList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override fun getAllStudents(): Flow<List<User>> = callbackFlow {
        // Agregamos listener para los cambios en la colección de usuarios
        val subscription = usersCollection
            .whereEqualTo("role", Role.Alumno) // Aplicamos el filtro de role Alumno
            .addSnapshotListener { querySnapshot, _ ->
                // Inicializamos lista mutable para almacenar los usuarios
                val studentsList = mutableListOf<User>()
                // Recorremos los documentos obtenidos de la consulta y los agregamos a la lista
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val student = documentSnapshot.toObject(User::class.java)
                    student?.let { studentsList.add(it) }
                }
                // Intentemos enviar la lista a través del flujo
                trySend(studentsList).isSuccess
            }
        // Esperamos a que se cierre el flujo para eliminar el listener
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun insertUser(user: User) {
        try {
            // Agregamos a la colección el nuevo usuario
            val document = usersCollection.add(user.toMap()).await()
            // Actualizamos el usuario copiando el ID asignado por Firestore al insertarlo
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