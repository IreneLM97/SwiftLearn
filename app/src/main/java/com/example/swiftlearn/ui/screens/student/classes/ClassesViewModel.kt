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
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de clases del profesor.
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
                // Obtenemos el usuario autentificado
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizar el estado de la pantalla con el usuario
                _classesUiState.update { it.copy(userLogged = userLogged) }

                // Combina los flujos de datos de profesores, anuncios y solicitudes
                combine(
                    userRepository.getAllProfessors(),
                    advertRepository.getAllAdverts(),
                    requestRepository.getAllRequestsByStudentId(userLogged._id)
                ) { professors, adverts, requests ->
                    val advertsByRequests = adverts.filter { advert ->
                        requests.find { it.advertId == advert._id } != null
                    }
                    val professorsByAdverts = professors.filter { professor ->
                        advertsByRequests.find { it.profId == professor._id } != null
                    }
                    Triple(professorsByAdverts, advertsByRequests, requests)
                }.collect { (professorsByAdverts, advertsByRequests, requests) ->
                    _classesUiState.update {
                        it.copy(
                            advertsList = advertsByRequests,
                            professorsList = professorsByAdverts,
                            pendingRequests = requests.filter { request -> request.status == Status.Pendiente.toString() },
                            acceptedRequests = requests.filter { request -> request.status == Status.Aceptada.toString() },
                            deniedRequests = requests.filter { request -> request.status == Status.Rechazada.toString() },
                        ) }

                    delay(500)
                    _classesUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    fun deleteRequest(request: Request) {
        viewModelScope.launch {
            requestRepository.deleteRequest(request)
        }
    }
}