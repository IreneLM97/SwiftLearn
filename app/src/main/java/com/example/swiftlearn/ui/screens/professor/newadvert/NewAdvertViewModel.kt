package com.example.swiftlearn.ui.screens.professor.newadvert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] para gestionar el estado y la lógica de la pantalla de nuevo anuncio.
 */
class NewAdvertViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository
): ViewModel() {
    // Estado de la interfaz de nuevo anuncio
    private val _advertUiState = MutableStateFlow(AdvertUiState())
    val advertUiState = _advertUiState.asStateFlow()

    // Variable para el control de la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    fun onFieldChanged(advertDetails: AdvertDetails) {
        _advertUiState.update { it.copy(advertDetails = advertDetails, isEntryValid = validateForm(advertDetails)) }
    }

    fun insertAdvert(advert: Advert) {
        // Actualizar estado de cargando a true
        _advertUiState.update { it.copy(isLoading = true) }

        // Agregamos el anuncio a la colección
        viewModelScope.launch {
            // Recogemos el Id del usuario y lo asignamos al profId del anuncio
            val profId = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())?._id
            val advertWithProfId = advert.copy(profId = profId.toString())

            // Insertamos el anuncio en la colección
            advertRepository.insertAdvert(advertWithProfId)

            // Actualizar estado de cargando a false
            _advertUiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Función para validar el formulario de nuevo anuncio.
     *
     * @param advertDetails Datos del anuncio recogidos del formulario.
     * @return true si el formulario es válido, false en caso contrario.
     */
    private fun validateForm(advertDetails: AdvertDetails): Boolean {
        return ValidationUtils.isSubjectValid(advertDetails.subject) &&
                ValidationUtils.isPriceValid(advertDetails.price) &&
                ValidationUtils.isOptionValid(advertDetails.classModes) &&
                ValidationUtils.isOptionValid(advertDetails.levels) &&
                ValidationUtils.isDescriptionValid(advertDetails.description)
    }
}