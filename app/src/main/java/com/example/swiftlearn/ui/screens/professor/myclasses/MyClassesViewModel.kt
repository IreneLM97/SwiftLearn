package com.example.swiftlearn.ui.screens.professor.myclasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.Status
import com.example.swiftlearn.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [MyClassesViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de mis clases.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 * @param requestRepository Repositorio para gestionar la colección solicitudes.
 */
class MyClassesViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de mis clases
    private val _myClassesUiState = MutableStateFlow(MyClassesUiState())
    val myClassesUiState = _myClassesUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos los datos del usuario desde el repositorio
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
                _myClassesUiState.update { it.copy(userLogged = userLogged) }

                // Obtenemos la lista de anuncios del profesor
                val advertsList = advertRepository.getAllAdvertsByProfId(userLogged._id)
                // Actualizamos el estado de la pantalla con la lista de anuncios
                _myClassesUiState.update { it.copy(advertsList = advertsList) }

                // Si la lista de anuncios está vacía, actualizamos el estado de la pantalla y salimos del ViewModelScope
                if (advertsList.isEmpty()) {
                    _myClassesUiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                // Obtenemos la lista de Ids de los anuncios del profesor
                val advertIds = advertsList.map { it._id }

                // Combinamos los flujos de datos de alumnos y solicitudes
                combine(
                    userRepository.getAllStudents(),
                    requestRepository.getAllRequestsByAdvertIds(advertIds)
                ) { students, requests ->
                    val studentsWithRequests = students.filter { student ->
                        requests.find { it.studentId == student._id } != null
                    }
                    Triple(studentsWithRequests, requests, null)
                }.collect { (studentsWithRequests, requests, _) ->
                    // Actualizamos el estado de la interfaz
                    _myClassesUiState.update {
                        it.copy(
                            studentsList = studentsWithRequests,
                            pendingRequests = requests.filter { request -> request.status == Status.Pendiente.toString() },
                            acceptedRequests = requests.filter { request -> request.status == Status.Aceptada.toString() },
                            deniedRequests = requests.filter { request -> request.status == Status.Rechazada.toString() },
                        ) }
                    
                    delay(500)
                    // Actualizamos estado de cargando a false
                    _myClassesUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función para actualizar el estado de una solicitud.
     *
     * @param request Solicitud a actualizar.
     */
    fun updateRequest(request: Request) {
        viewModelScope.launch {
            requestRepository.updateRequest(request)
        }
    }
}