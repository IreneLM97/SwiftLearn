package com.example.swiftlearn.ui.screens.professor.editadvert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.ui.screens.professor.newadvert.AdvertDetails
import com.example.swiftlearn.ui.screens.professor.newadvert.AdvertUiState
import com.example.swiftlearn.ui.screens.professor.newadvert.toAdvertDetails
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [EditAdvertViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de editar anuncio.
 *
 * @param savedStateHandle Maneja el estado guardado de la aplicación.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 */
class EditAdvertViewModel(
    savedStateHandle: SavedStateHandle,
    val advertRepository: AdvertRepository
): ViewModel() {
    // Estado de la interfaz de editar anuncio
    private val _advertUiState = MutableStateFlow(AdvertUiState())
    val advertUiState = _advertUiState.asStateFlow()

    // Recogemos el Id del anuncio
    private val advertId: String = checkNotNull(savedStateHandle[EditAdvertDestination.advertIdArg])

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            _advertUiState.update { it.copy(
                advertDetails = advertRepository.getAdvertById(advertId)?.toAdvertDetails() ?: AdvertDetails(),
                isEntryValid = true
            ) }
        }
    }

    /**
     * Función que se ejecuta cuando cambia un campo del formulario.
     *
     * @param advertDetails Detalles del formulario.
     */
    fun onFieldChanged(advertDetails: AdvertDetails) {
        // Actualizamos el estado de la interfaz
        _advertUiState.update { it.copy(advertDetails = advertDetails, isEntryValid = validateForm(advertDetails)) }
    }

    /**
     * Función que actualiza un anuncio con nuevos datos.
     *
     * @param advert Anuncio con los datos actualizados.
     */
    fun updateAdvert(advert: Advert) {
        // Actualizamos estado de cargando a true
        _advertUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Actualizamos el anuncio con los nuevos datos
            advertRepository.updateAdvert(advert)

            // Actualizamos estado de cargando a false
            _advertUiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Función para validar el formulario de editar anuncio.
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