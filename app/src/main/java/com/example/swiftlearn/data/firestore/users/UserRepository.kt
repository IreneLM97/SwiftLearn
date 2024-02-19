package com.example.swiftlearn.data.firestore.users

import com.example.swiftlearn.model.User
import kotlinx.coroutines.flow.Flow

/**
 * [UserRepository] es una interfaz que define las operaciones para gestionar la colección de usuarios en Firestore.
 */
interface UserRepository {
    /**
     * Obtiene un usuario dado su ID de autenticación en Firebase.
     *
     * @param authId ID de autenticación en Firebase del usuario.
     * @return Usuario si se encuentra, de lo contrario null.
     */
    suspend fun getUserByAuthId(authId: String): User?

    /**
     * Obtiene todos los usuarios con el rol de Profesor como un flujo.
     *
     * @return Flujo de lista de usuarios profesores.
     */
    fun getAllProfessors(): Flow<List<User>>

    /**
     * Obtiene todos los usuarios con el rol de Alumno como un flujo.
     *
     * @return Flujo de lista de usuarios alumnos.
     */
    fun getAllStudents(): Flow<List<User>>

    /**
     * Inserta un nuevo usuario a la colección.
     *
     * @param user Usuario a insertar.
     */
    suspend fun insertUser(user: User)

    /**
     * Actualiza un usuario dado.
     *
     * @param user Usuario a actualizar.
     */
    suspend fun updateUser(user: User)

    /**
     * Elimina un usuario dado.
     *
     * @param user Usuario a eliminar.
     */
    suspend fun deleteUser(user: User)
}