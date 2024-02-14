package com.example.swiftlearn.ui.screens.student.adverts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.screens.student.AdvertDetail
import com.example.swiftlearn.ui.screens.student.AdvertsBar
import com.example.swiftlearn.ui.screens.student.AdvertsList
import com.example.swiftlearn.ui.screens.student.AdvertsListAndDetail
import com.example.swiftlearn.ui.screens.utils.AdvertsContentType

/**
 * Función que representa la pantalla principal de los anuncios.
 *
 * @param viewModel ViewModel que gestiona el estado de la interfaz de usuario.
 * @param windowSize Clasificación del tamaño de la ventana.
 * @param onSendButtonClick Función lambda que se invoca cuando se hace click en el botón de enviar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertsScreen(
    windowSize: WindowWidthSizeClass,
    onSendButtonClick: (String) -> Unit,
    navigateToClasses: () -> Unit,
    viewModel: AdvertsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    // Obtenemos administrador del foco de la aplicación
    val focusManager = LocalFocusManager.current

    // Guardamos el estado de la pantalla de anuncios
    val advertsUiState = viewModel.advertsUiState.collectAsState().value

    // Determinamos el tipo de contenido en función del tamaño de la ventana
    val contentType = when (windowSize) {
        // Para ventanas compactas o de tamaño medio, mostrar solo la lista de anuncios
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> AdvertsContentType.ListOnly

        // Para ventanas expandidas, mostrar tanto la lista como los detalles de los anuncios
        WindowWidthSizeClass.Expanded -> AdvertsContentType.ListAndDetail
        else -> AdvertsContentType.ListOnly
    }

    // Mostramos el icono cargando si está cargando
    if(advertsUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                if (contentType == AdvertsContentType.ListOnly && !advertsUiState.isShowingListPage) {
                    // Barra superior personalizada
                    AdvertsBar(
                        onBackButtonClick = { viewModel.navigateToListAdvertsPage() }
                    )
                }
            }
        ) { innerPadding ->
            // Contenido principal de la pantalla en función del tamaño de la pantalla
            if (contentType == AdvertsContentType.ListAndDetail) { // tamaño pantalla expanded
                // Mostramos lista y detalles de anuncios
                AdvertsListAndDetail(
                    windowSize = windowSize,
                    advertsUiState = advertsUiState,
                    notFoundMessage = stringResource(id = R.string.not_found_adverts),
                    onQueryChange = {
                        viewModel.onQueryChange(it)
                        if (it.isEmpty()) focusManager.clearFocus()
                    },
                    onAdvertClick = {
                        viewModel.updateCurrentAdvert(it)
                    },
                    onFavoriteButtonClick = {
                        viewModel.toggleAdvertFavoriteState(it)
                    },
                    onRequestConfirm = {
                        viewModel.insertRequest(it)
                        navigateToClasses()
                    },
                    onSendButtonClick = onSendButtonClick,
                    contentPadding = innerPadding,
                    contentType = contentType,
                    modifier = Modifier.fillMaxWidth()
                )
            } else { // tamaño pantalla standard
                if (advertsUiState.isShowingListPage) {
                    // Mostramos lista de anuncios
                    AdvertsList(
                        advertsUiState = advertsUiState,
                        notFoundMessage = stringResource(id = R.string.not_found_adverts),
                        onQueryChange = {
                            viewModel.onQueryChange(it)
                            if (it.isEmpty()) focusManager.clearFocus()
                        },
                        onAdvertClick = {
                            viewModel.updateCurrentAdvert(it)
                            viewModel.navigateToDetailAdvertPage()
                        },
                        onFavoriteButtonClick = {
                            viewModel.toggleAdvertFavoriteState(it)
                        },
                        onSendButtonClick = onSendButtonClick,
                        contentPadding = innerPadding,
                        contentType = contentType,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                    )
                } else {
                    // Obtener el profesor correspondiente al anuncio
                    val professor = advertsUiState.professorsList.find { it._id == advertsUiState.currentAdvert.profId }
                    professor?.let {
                        // Mostramos detalles de un anuncio específico
                        AdvertDetail(
                            windowSize = windowSize,
                            studentId = advertsUiState.user._id,
                            advert = advertsUiState.currentAdvert,
                            professor = professor,
                            onRequestConfirm = {
                                viewModel.insertRequest(it)
                                navigateToClasses()
                            },
                            contentPadding = innerPadding
                        )
                    }
                }
            }
        }
    }
}
