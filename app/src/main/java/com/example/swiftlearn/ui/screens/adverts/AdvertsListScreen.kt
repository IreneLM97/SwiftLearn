package com.example.swiftlearn.ui.screens.adverts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.OptionsSection
import com.example.swiftlearn.ui.components.SearchTextField
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
fun AdvertsListScreen(
    windowSize: WindowWidthSizeClass,
    viewModel: AdvertsListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSendButtonClick: (String) -> Unit = {}
){
    // Obtenemos administrador del foco de la aplicación
    val focusManager = LocalFocusManager.current

    // Guardamos el estado de la pantalla de anuncios
    val advertsListUiState = viewModel.advertsListUiState.collectAsState().value

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
    if(advertsListUiState.loadingState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                if (contentType == AdvertsContentType.ListOnly && !advertsListUiState.isShowingListPage) {
                    // Barra superior personalizada
                    AdvertsListBar(
                        onBackButtonClick = { viewModel.navigateToListAdvertsPage() }
                    )
                }
            }
        ) { innerPadding ->
            // Contenido principal de la pantalla en función del tamaño de la pantalla
            if (contentType == AdvertsContentType.ListAndDetail) { // tamaño pantalla expanded
                // Mostramos lista y detalles de anuncios
                AdvertsListAndDetail(
                    advertsListUiState = advertsListUiState,
                    advertsList = advertsListUiState.advertsList,
                    professorsList = advertsListUiState.professorsList,
                    onQueryChange = {
                        viewModel.onQueryChange(it)
                        if (it.isEmpty()) focusManager.clearFocus()
                    },
                    onAdvertClick = {
                        viewModel.updateCurrentPlace(it)
                    },
                    onFavoriteButtonClick = {
                        viewModel.toggleAdvertFavoriteState(it)
                    },
                    onSendButtonClick = onSendButtonClick,
                    contentPadding = innerPadding,
                    contentType = contentType,
                    modifier = Modifier.fillMaxWidth()
                )
            } else { // tamaño pantalla standard
                if (advertsListUiState.isShowingListPage) {
                    // Mostramos lista de anuncios
                    AdvertsList(
                        advertsListUiState = advertsListUiState,
                        advertsList = advertsListUiState.advertsList,
                        professorsList = advertsListUiState.professorsList,
                        onQueryChange = {
                            viewModel.onQueryChange(it)
                            if (it.isEmpty()) focusManager.clearFocus()
                        },
                        onAdvertClick = {
                            viewModel.updateCurrentPlace(it)
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
                    val professor =
                        advertsListUiState.professorsList.find { it._id == advertsListUiState.currentAdvert.profId }
                    professor?.let {
                        // Mostramos detalles de un anuncio específico
                        AdvertDetail(
                            advert = advertsListUiState.currentAdvert,
                            professor = professor,
                            onFavoriteButtonClick = {
                                viewModel.toggleAdvertFavoriteState(it)
                            },
                            onSendButtonClick = onSendButtonClick,
                            contentPadding = innerPadding
                        )
                    }
                }
            }
        }
    }
}

/**
 * Función que representa la barra superior de la pantalla.
 *
 * @param onBackButtonClick Función lambda que se invoca al pulsar el botón de retroceso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvertsListBar(
    onBackButtonClick: () -> Unit
) {
    // Barra superior personalizada
    TopAppBar(
        title = { Text("") },
        // Icono de navegación que corresponde a una flecha hacia atrás
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.description_back_icon)
                )
            }
        },
        // Color personalizado para la barra superior
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

/**
 * Función que representa la lista de anuncios.
 *
 * @param modifier Modificador opcional para aplicar al diseño de la lista.
 * @param advertsListUiState Estado de la interfaz de usuario.
 * @param advertsList Lista de anuncios a mostrar.
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio.
 * @param contentPadding Espaciado alrededor del contenido de la lista.
 * @param contentType Indica tipo de contenido de la pantalla (ListOnly o ListAndDetail).
 */
@Composable
private fun AdvertsList(
    modifier: Modifier = Modifier,
    advertsListUiState: AdvertsListUiState = AdvertsListUiState(),
    advertsList: List<Advert>,
    professorsList: List<User>,
    onQueryChange: (String) -> Unit,
    onAdvertClick: (Advert) -> Unit,
    onFavoriteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentType: AdvertsContentType = AdvertsContentType.ListOnly
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = modifier.height(20.dp))

        // Mostramos campo de búsqueda de clases en función de la asignatura
        SearchTextField(
            query = advertsListUiState.searchQuery,
            onQueryChange = onQueryChange
        )
        Spacer(modifier = modifier.height(10.dp))

        // Filtramos la lista de anuncios por la asignatura del anuncio
        val filteredAdverts = if (advertsListUiState.searchQuery.isNotEmpty()) {
            advertsList.filter { it.subject.contains(advertsListUiState.searchQuery, ignoreCase = true) }
        } else {
            advertsList
        }

        if (filteredAdverts.isEmpty()) {
            // Mostrar mensaje cuando no se encuentran anuncios que coincidan con la asignatura
            Text(
                text = stringResource(id = R.string.no_matching_adverts),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            )
        } else {
            // Mostrar la lista de anuncios filtrada
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .padding(bottom = 62.dp)
            ) {
                items(filteredAdverts) { advert ->
                    // Obtener el profesor correspondiente al anuncio
                    val professor = professorsList.find { it._id == advert.profId }
                    
                    // Comprobamos si es favorito o no
                    val favorite = advertsListUiState.favoritesList.find {
                        it.userId == advertsListUiState.user._id && it.advertId == advert._id
                    }

                    professor?.let {
                        // Representa un elemento de la lista
                        AdvertItem(
                            advertsListUiState = advertsListUiState,
                            professor = it,
                            advert = advert,
                            isFavorite = favorite != null,
                            onAdvertClick = onAdvertClick,
                            onFavoriteButtonClick = onFavoriteButtonClick,
                            onSendButtonClick = onSendButtonClick,
                            contentType = contentType
                        )
                    }
                }
            }
        }

        // Agrega margen al final de la lista de anuncios
        Spacer(modifier = modifier.height(20.dp))
    }
}


/**
 * Función que representa un elemento individual en la lista de lugares.
 *
 * @param modifier Modificador opcional para aplicar al diseño del elemento.
 * @param advertsListUiState Estado de la interfaz de usuario.
 * @param advert Anuncio que se está representando.
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio.
 * @param onFavoriteButtonClick Función lambda que se invoca cuando se hace click en el botón de favorito.
 * @param contentType Indica tipo de contenido de la pantalla (ListOnly o ListAndDetail).
 */
@Composable
private fun AdvertItem(
    modifier: Modifier = Modifier,
    advertsListUiState: AdvertsListUiState = AdvertsListUiState(),
    professor: User,
    advert: Advert,
    isFavorite: Boolean,
    onAdvertClick: (Advert) -> Unit = {},
    onFavoriteButtonClick: (Advert) -> Unit = {},
    onSendButtonClick: (String) -> Unit = {},
    contentType: AdvertsContentType = AdvertsContentType.ListOnly
) {
    // Comprobamos si está seleccionado el item y estamos en vista ListAndDetail
    // para personalizar el fondo del item cuando esté seleccionado
    val isSelected = advertsListUiState.currentAdvert._id == advert._id && contentType == AdvertsContentType.ListAndDetail

    // Resumen de la información del anuncio
    val advertSummary = stringResource(
        R.string.advert_summary,
        advert.subject,
        advert.price,
        advert.description
    )

    // Diseño del item de la lista
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .clickable { onAdvertClick(advert) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = if (isSelected) colorResource(id = R.color.my_pink)
                            else colorResource(id = R.color.my_light_pink)
                )
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.my_light_purple),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Nombre del profesor
                    Text(
                        text = professor.username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.weight(1f)
                    )

                    // Botón del icono de favorito
                    IconButton(
                        onClick = { onFavoriteButtonClick(advert) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        // Icono de favorito
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            tint = if (isFavorite) Color.Red else colorResource(id = R.color.my_dark_gray),
                            contentDescription = null
                        )
                    }

                    // Botón del icono de compartir
                    IconButton(
                        onClick = { onSendButtonClick(advertSummary) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        // Icono de compartir
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.Share,
                            tint = colorResource(id = R.color.my_dark_gray),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Asignatura y precio del anuncio
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = advert.subject,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(
                            id = R.string.icon_euro_hour,
                            advert.price.toString()
                        ),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.my_dark_purple)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

                // Información de la ubicación
                IconWithText(
                    icon = Icons.Outlined.LocationOn,
                    text = stringResource(id = R.string.direction_professor, professor.address, professor.postal),
                    iconSize = 20.dp,
                    textSize = 15.sp
                )

                // Información del modo de clase
                IconWithText(
                    icon = Icons.Outlined.House,
                    text = advert.classModes,
                    iconSize = 20.dp,
                    textSize = 15.sp
                )

                // Información de los niveles de clase
                IconWithText(
                    icon = Icons.Outlined.Leaderboard,
                    text = advert.levels,
                    iconSize = 20.dp,
                    textSize = 15.sp
                )
            }
        }
    }
}

/**
 * Función que representa la pantalla de detalles de un lugar concreto.
 *
 * @param advert Anuncio del que se muestran detalles.
 * @param professor Profesor que publicó el anuncio del que se muestran los detalles.
 * @param modifier Modificador opcional para aplicar al diseño.
 * @param onFavoriteButtonClick Función lambda que se invoca cuando se pulsa en favorito.
 * @param onSendButtonClick Función lambda que se invoca cuando se pulsa en enviar.
 * @param contentPadding Espaciado alrededor del contenido de la pantalla de detalles.
 */
@Composable
private fun AdvertDetail(
    advert: Advert,
    professor: User,
    modifier: Modifier = Modifier,
    onFavoriteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Estado del scroll
    val scrollState = rememberScrollState()

    // Resumen de la información del anuncio
    val advertSummary = stringResource(
        R.string.advert_summary,
        advert.subject,
        advert.price,
        advert.description
    )

    // Estructura de la información detallada del anuncio
    Box(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(contentPadding)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            // Nombre del profesor
            Text(
                text = professor.username,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.my_dark_gray),
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            // Mensaje información del profesor
            Text(
                text = stringResource(R.string.info_contact_label),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                color = colorResource(id = R.color.my_dark_gray),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            // Dirección del profesor
            IconWithText(
                icon = Icons.Outlined.LocationOn,
                text = stringResource(id = R.string.direction_professor, professor.address, professor.postal),
                iconSize = 30.dp,
                textSize = 20.sp
            )

            // Teléfono del profesor
            IconWithText(
                icon = Icons.Outlined.Phone,
                text = professor.phone,
                iconSize = 30.dp,
                textSize = 20.sp
            )

            // Email del profesor
            IconWithText(
                icon = Icons.Outlined.Email,
                text = professor.email,
                iconSize = 30.dp,
                textSize = 20.sp
            )
            Spacer(Modifier.height(20.dp))

            // Mensaje información del anuncio
            Text(
                text = stringResource(R.string.info_advert_label),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                color = colorResource(id = R.color.my_dark_gray),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            // Asignatura y precio del anuncio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = advert.subject,
                    color = colorResource(id = R.color.my_dark_gray),
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.icon_euro_hour, advert.price.toString()),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.my_dark_purple)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Opciones de modalidad de clase
            val classModesSet = remember {
                mutableStateOf(advert.classModes.split(", ").mapNotNull { value ->
                    ClassMode.values().find { it.name == value }
                }.toSet())
            }
            OptionsSection(
                title = stringResource(id = R.string.class_mode_advert),
                options = listOf(
                    ClassMode.Presencial to stringResource(id = R.string.presencial_label),
                    ClassMode.Online to stringResource(id = R.string.online_label),
                    ClassMode.Hibrido to stringResource(id = R.string.hibrido_label)
                ),
                selectedOptions = classModesSet.value,
                isSelectable = false
            )

            // Opciones de niveles de clase
            val levelsSet = remember {
                mutableStateOf(advert.levels.split(", ").mapNotNull { value ->
                    Level.values().find { it.name == value }
                }.toSet())
            }
            OptionsSection(
                title = stringResource(id = R.string.levels_advert),
                options = listOf(
                    Level.Primaria to stringResource(id = R.string.primaria_label),
                    Level.ESO to stringResource(id = R.string.eso_label),
                    Level.Bachillerato to stringResource(id = R.string.bachillerato_label),
                    Level.FP to stringResource(id = R.string.fp_label),
                    Level.Universidad to stringResource(id = R.string.universidad_label),
                    Level.Adultos to stringResource(id = R.string.adultos_label)
                ),
                selectedOptions = levelsSet.value,
                isSelectable = false
            )

            // Descripción del anuncio
            IconWithText(
                icon = Icons.Outlined.Description,
                text = advert.description,
                iconSize = 30.dp,
                textSize = 20.sp
            )
            Spacer(Modifier.height(20.dp))

            // Botón para solicitar una clase
            ButtonWithText(
                label = stringResource(R.string.request_class_label),
                buttonColor = colorResource(id = R.color.my_dark_purple),
                textColor = Color.White
            )
            Spacer(Modifier.height(100.dp))
        }
    }
}

/**
 * Función que representa la pantalla de lista de anuncios y detalles de un anuncio.
 *
 * @param modifier Modificador opcional para aplicar al diseño.
 * @param advertsListUiState Estado actual de la interfaz de lista de anuncios.
 * @param advertsList Lista de anuncios a mostrar.
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio de la lista.
 * @param onFavoriteButtonClick Función lambda que se ejecuta cuando se marca como favorito un anuncio.
 * @param onSendButtonClick Función lambda que se invoca cuando se hace click en el botón de enviar.
 * @param contentPadding Espaciado alrededor del contenido de la pantalla.
 * @param contentType Indica tipo de contenido de la pantalla (ListOnly o ListAndDetail).
 */
@Composable
private fun AdvertsListAndDetail(
    modifier: Modifier = Modifier,
    advertsListUiState: AdvertsListUiState = AdvertsListUiState(),
    advertsList: List<Advert>,
    professorsList: List<User>,
    onQueryChange: (String) -> Unit,
    onAdvertClick: (Advert) -> Unit,
    onFavoriteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentType: AdvertsContentType = AdvertsContentType.ListOnly
) {
    Row(
        modifier = modifier
    ) {
        // Representa la lista de anuncios
        AdvertsList(
            advertsListUiState = advertsListUiState,
            advertsList = advertsList,
            professorsList = professorsList,
            onQueryChange = onQueryChange,
            onAdvertClick = onAdvertClick,
            onFavoriteButtonClick = onFavoriteButtonClick,
            onSendButtonClick = onSendButtonClick,
            contentPadding = contentPadding,
            contentType = contentType,
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        )

        // Obtener el usuario correspondiente al anuncio
        val professor = professorsList.find { it._id == advertsListUiState.currentAdvert.profId }
        professor?.let {
            // Representa los detalles de un anuncio específico
            AdvertDetail(
                advert = advertsListUiState.currentAdvert,
                professor = professor,
                onFavoriteButtonClick = onFavoriteButtonClick,
                onSendButtonClick = onSendButtonClick,
                modifier = Modifier.weight(3f),
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
private fun IconWithText(
    icon: ImageVector,
    text: String,
    iconSize: Dp,
    textSize: TextUnit
    ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.my_dark_gray),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(iconSize)
        )
        Text(
            text = text,
            fontSize = textSize,
            color = colorResource(id = R.color.my_dark_gray)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

/**
 * Función que previsualiza la lista de anuncios.
 */
@Preview
@Composable
fun AdvertsListPreview() {
    AdvertsList(
        advertsList = listOf(
            Advert(
                profId = "1",
                subject = "Matematicas",
                price = 15,
                classModes = "Presencial,Hibrido",
                levels = "Bachillerato"
            )
        ),
        professorsList = listOf(
            User(
                _id = "1",
                username = "Maria",
                phone = "657565343",
                address = "Calle Real",
                postal = "56474",
                email = "maria@gmail.com"
            )
        ),
        onQueryChange = {},
        onAdvertClick = {},
        onFavoriteButtonClick = {},
        onSendButtonClick = {}
    )
}

@Preview
@Composable
fun AdvertDetailPreview() {
    AdvertDetail(
        advert = Advert(
            subject = "Lengua",
            price = 12,
            classModes = "Presencial,Hibrido",
            levels = "Bachillerato"
        ),
        professor = User(
            _id = "1",
            username = "Maria",
            phone = "657565343",
            address = "Calle Real",
            postal = "56474",
            email = "maria@gmail.com"
        ),
        onFavoriteButtonClick = {},
        onSendButtonClick = {}
    )
}
