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
 * [NewAdvertViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de crear nuevo anuncio.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 */
class NewAdvertViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository
): ViewModel() {
    // Estado de la interfaz de nuevo anuncio
    private val _advertUiState = MutableStateFlow(AdvertUiState())
    val advertUiState = _advertUiState.asStateFlow()

    // Variable para la autentificación de usuarios
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param advertDetails Detalles del formulario.
     */
    fun onFieldChanged(advertDetails: AdvertDetails) {
        _advertUiState.update { it.copy(advertDetails = advertDetails, isEntryValid = validateForm(advertDetails)) }
    }

    /**
     * Función para insertar un nuevo anuncio.
     *
     * @param advert Anuncio a insertar.
     */
    fun insertAdvert(advert: Advert) {
        // Actualizamos estado de cargando a true
        _advertUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Recogemos el Id del usuario y lo asignamos al profId del anuncio
            val profId = userRepository.getUserByAuthId(auth.currentUser?.uid.toString())?._id
            val advertWithProfId = advert.copy(profId = profId.toString())

            // Insertamos el anuncio en la colección
            advertRepository.insertAdvert(advertWithProfId)

            // Actualizamos estado de cargando a false
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