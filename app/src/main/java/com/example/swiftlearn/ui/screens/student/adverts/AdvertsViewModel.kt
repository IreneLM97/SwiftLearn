package com.example.swiftlearn.ui.screens.student.adverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftlearn.data.firestore.adverts.AdvertRepository
import com.example.swiftlearn.data.firestore.favorites.FavoriteRepository
import com.example.swiftlearn.data.firestore.requests.RequestRepository
import com.example.swiftlearn.data.firestore.users.UserRepository
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Favorite
import com.example.swiftlearn.model.Request
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
 * [AdvertsViewModel] es un [ViewModel] que gestiona el estado y la lógica de la pantalla de anuncios.
 *
 * @param userRepository Repositorio para gestionar la colección usuarios.
 * @param advertRepository Repositorio para gestionar la colección anuncios.
 * @param favoriteRepository Repositorio para gestionar la colección favoritos.
 * @param requestRepository Repositorio para gestionar la colección solicitudes.
 */
class AdvertsViewModel(
    val userRepository: UserRepository,
    val advertRepository: AdvertRepository,
    val favoriteRepository: FavoriteRepository,
    val requestRepository: RequestRepository
): ViewModel() {
    // Estado de la interfaz de anuncios
    private val _advertsUiState = MutableStateFlow(AdvertsUiState())
    val advertsUiState = _advertsUiState.asStateFlow()

    // Inicialización del ViewModel
    init {
        viewModelScope.launch {
            try {
                // Obtenemos los datos del usuario autentificado
                val userLogged = userRepository.getUserByAuthId(Firebase.auth.currentUser?.uid.toString()) ?: User()
                // Actualizamos el estado de la pantalla con los datos del usuario obtenidos
                _advertsUiState.update { it.copy(userLogged = userLogged) }

                // Combinamos los flujos de datos de profesores, anuncios y favoritos
                combine(
                    userRepository.getAllProfessors(),
                    advertRepository.getAllAdverts(),
                    favoriteRepository.getAllFavoritesByStudentId(userLogged._id)
                ) { professors, adverts, favorites  ->
                    Triple(professors, adverts, favorites)
                }.collect { (professors, adverts, favorites) ->
                    // Actualizamos el estado de la interfaz con los flujos obtenidos
                    _advertsUiState.update {
                        it.copy(
                            professorsList = professors,
                            advertsList = adverts,
                            favoritesList = favorites,
                            currentAdvert = adverts.firstOrNull() ?: Advert()
                        )
                    }

                    delay(1000)
                    // Actualizamos estado de cargando a false
                    _advertsUiState.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Función que se ejecuta cuando cambia la consulta de búsqueda.
     *
     * @param searchQuery Consulta de búsqueda actual.
     */
    fun onQueryChange(searchQuery: String) {
        _advertsUiState.update { it.copy(searchQuery = searchQuery) }
    }

    /**
     * Función que actualiza el anuncio seleccionado.
     *
     * @param selectedAdvert Anuncio seleccionado.
     */
    fun updateCurrentAdvert(selectedAdvert: Advert) {
        _advertsUiState.update { it.copy(currentAdvert = selectedAdvert) }
    }

    /**
     * Función que actualiza el estado de favorito de un anuncio.
     *
     * @param advert Anuncio en el que se pulsa favorito.
     */
    fun toggleAdvertFavoriteState(advert: Advert) {
        viewModelScope.launch {
            // Si ese anuncio era favorito -> Se elimina el favorito
            // Si ese anuncio no era favorito (get devuelve null) -> Se inserta el favorito
            val userId = _advertsUiState.value.userLogged._id
            favoriteRepository.getFavoriteByInfo(studentId = userId, advertId = advert._id)?.let { favorite ->
                favoriteRepository.deleteFavorite(favorite)
            } ?: favoriteRepository.insertFavorite(Favorite(studentId = userId, advertId = advert._id))
        }
    }

    /**
     * Función para insertar una nueva solicitud de clase.
     *
     * @param request Solicitud de clase a insertar.
     */
    fun insertRequest(request: Request) {
        viewModelScope.launch {
            requestRepository.insertRequest(request)
        }
    }

    /**
     * Navega a la página que muestra la lista de anuncios.
     */
    fun navigateToListAdvertsPage() {
        _advertsUiState.update { it.copy(isShowingListPage = true) }
    }

    /**
     * Navega a la página que muestra los detalles de un anuncio específico.
     */
    fun navigateToDetailAdvertPage() {
        _advertsUiState.update { it.copy(isShowingListPage = false) }
    }
}