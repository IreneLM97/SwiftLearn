package com.example.swiftlearn.ui.screens.student.classes

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
 * [ClassesViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de solicitudes de clases.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 * @param requestRepository Repositorio para gestionar la colección solicitudes.
 */
class ClassesViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de clases
    private val _classesUiState = MutableStateFlow(ClassesUiState())
    val classesUiState = _classesUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos los datos del usuario autentificado
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
                _classesUiState.update { it.copy(userLogged = userLogged) }

                // Combinamos los flujos de datos de profesores, anuncios y solicitudes
                combine(
                    userRepository.getAllProfessors(),
                    advertRepository.getAllAdverts(),
                    requestRepository.getAllRequestsByStudentId(userLogged._id)
                ) { professors, adverts, requests ->
                    // Filtramos anuncios y profesores asociados a alguna solicitud
                    val advertsByRequests = adverts.filter { advert ->
                        requests.find { it.advertId == advert._id } != null
                    }
                    val professorsByAdverts = professors.filter { professor ->
                        advertsByRequests.find { it.profId == professor._id } != null
                    }
                    Triple(professorsByAdverts, advertsByRequests, requests)
                }.collect { (professorsByAdverts, advertsByRequests, requests) ->
                    // Actualizamos el estado de la interfaz
                    _classesUiState.update {
                        it.copy(
                            advertsList = advertsByRequests,
                            professorsList = professorsByAdverts,
                            pendingRequests = requests.filter { request -> request.status == Status.Pendiente.toString() },
                            acceptedRequests = requests.filter { request -> request.status == Status.Aceptada.toString() },
                            deniedRequests = requests.filter { request -> request.status == Status.Rechazada.toString() },
                        ) }

                    delay(500)
                    // Actualizamos estado de cargando a false
                    _classesUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función para eliminar una solicitud de clase.
     *
     * @param request Solicitud a eliminar.
     */
    fun deleteRequest(request: Request) {
        viewModelScope.launch {
            requestRepository.deleteRequest(request)
        }
    }
}