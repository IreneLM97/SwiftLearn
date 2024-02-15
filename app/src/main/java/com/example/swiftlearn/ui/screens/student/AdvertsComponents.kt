package com.example.swiftlearn.ui.screens.student

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.zIndex
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.MultiOptionsSectionImmutable
import com.example.swiftlearn.ui.components.RequestClassDialog
import com.example.swiftlearn.ui.components.SearchTextField
import com.example.swiftlearn.ui.screens.student.adverts.AdvertsUiState
import com.example.swiftlearn.ui.screens.student.favorites.FavoritesUiState
import com.example.swiftlearn.ui.screens.utils.AdvertsContentType

/**
 * Función que representa la barra superior de la pantalla.
 *
 * @param onBackButtonClick Función lambda que se invoca al pulsar el botón de retroceso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertsBar(
    onBackButtonClick: () -> Unit
) {
    // Barra superior personalizada
    TopAppBar(
        title = { Text("") },
        // Icono de navegación que corresponde a una flecha hacia atrás
        navigationIcon = {
            Box(
                modifier = Modifier
                    .clickable { onBackButtonClick() }
                    .padding(8.dp)
                    .size(35.dp)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.description_back_icon),
                    tint = Color.Black
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
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio.
 * @param contentPadding Espaciado alrededor del contenido de la lista.
 * @param contentType Indica tipo de contenido de la pantalla (ListOnly o ListAndDetail).
 */
@Composable
fun AdvertsList(
    notFoundMessage: String,
    onQueryChange: (String) -> Unit,
    onAdvertClick: (Advert) -> Unit,
    onFavoriteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentType: AdvertsContentType = AdvertsContentType.ListOnly,
    advertsUiState: AdvertsUiState? = null,
    favoritesUiState: FavoritesUiState? = null
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = modifier.height(20.dp))

        // Mostramos campo de búsqueda de clases en función de la asignatura
        val searchQuery = advertsUiState?.searchQuery ?: favoritesUiState?.searchQuery ?: ""
        SearchTextField(
            query = searchQuery,
            onQueryChange = onQueryChange
        )
        Spacer(modifier = modifier.height(10.dp))

        // Filtramos la lista de anuncios por la asignatura del anuncio
        val advertsList = advertsUiState?.advertsList ?: favoritesUiState?.advertsList ?: emptyList()
        val filteredAdverts = if (searchQuery.isNotEmpty()) {
            advertsList.filter { it.subject.contains(searchQuery, ignoreCase = true) }
        } else {
            advertsList
        }

        if (filteredAdverts.isEmpty()) {
            // Mostrar mensaje cuando no se encuentran anuncios que coincidan con la asignatura
            Text(
                text = notFoundMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
                    .height(700.dp)
            )
        } else {
            // Mostrar la lista de anuncios filtrada
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .padding(bottom = 60.dp)
                    .height(700.dp)
            ) {
                items(filteredAdverts) { advert ->
                    // Obtener el profesor correspondiente al anuncio
                    val professorsList = advertsUiState?.professorsList ?: favoritesUiState?.professorsList ?: emptyList()
                    val professor = professorsList.find { it._id == advert.profId }

                    // Comprobamos si es favorito o no
                    val favoritesList = advertsUiState?.favoritesList ?: favoritesUiState?.favoritesList ?: emptyList()
                    val user = advertsUiState?.user ?: favoritesUiState?.user ?: User()
                    val isFavorite = favoritesList.any {
                        it.userId == user._id && it.advertId == advert._id
                    }

                    // Comprobamos si está seleccionado el item y estamos en vista ListAndDetail
                    // para personalizar el fondo del item cuando esté seleccionado
                    val currentAdvert = advertsUiState?.currentAdvert ?: favoritesUiState?.currentAdvert ?: Advert()
                    val isSelected = currentAdvert._id == advert._id && contentType == AdvertsContentType.ListAndDetail

                    professor?.let {
                        // Representa un elemento de la lista
                        AdvertItem(
                            professor = it,
                            advert = advert,
                            isFavorite = isFavorite,
                            isSelected = isSelected,
                            onAdvertClick = onAdvertClick,
                            onFavoriteButtonClick = onFavoriteButtonClick,
                            onSendButtonClick = onSendButtonClick
                        )
                    }
                }
            }
        }

        // Agrega margen al final de la lista de anuncios
        Spacer(modifier = modifier.height(40.dp))
    }
}

/**
 * Función que representa un elemento individual en la lista de lugares.
 *
 * @param modifier Modificador opcional para aplicar al diseño del elemento.
 * @param advert Anuncio que se está representando.
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio.
 * @param onFavoriteButtonClick Función lambda que se invoca cuando se hace click en el botón de favorito.
 */
@Composable
fun AdvertItem(
    professor: User,
    advert: Advert,
    isFavorite: Boolean,
    isSelected: Boolean,
    onAdvertClick: (Advert) -> Unit,
    onFavoriteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Resumen de la información del anuncio
    val advertSummary = stringResource(
        R.string.advert_summary,
        advert.subject,
        advert.price,
        professor.username,
        professor.phone,
        professor.email
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
                    text = stringResource(id = R.string.direction_info, professor.address, professor.postal),
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
 * @param contentPadding Espaciado alrededor del contenido de la pantalla de detalles.
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun AdvertDetail(
    windowSize: WindowWidthSizeClass,
    studentId: String,
    advert: Advert,
    professor: User,
    onRequestConfirm: (Request) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Estado del scroll
    val scrollState = rememberScrollState()

    // Estado booleano para controlar si el diálogo de solicitar clase está abierto o no
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // Mostramos el modal de solicitar clase si showDialog es true
    if (showDialog) {
        RequestClassDialog (
            windowSize = windowSize,
            studentId = studentId,
            advertId = advert._id,
            onRequestConfirm = onRequestConfirm,
            onRequestCancel = {
                showDialog = false
            }
        )
    }

    // Botón para solicitar una clase (versión movil)
    if(windowSize != WindowWidthSizeClass.Expanded) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .zIndex(2f),
        ) {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape,
                modifier = Modifier
                    .width(120.dp)
                    .height(35.dp)
                    .align(Alignment.TopEnd),
                containerColor = colorResource(id = R.color.my_dark_purple)
            ) {
                Text(
                    text = stringResource(id = R.string.request_button_label),
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

    // Estructura de la información detallada del anuncio
    Row(
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

            // Columna contenedora de la información del profesor
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        colorResource(id = R.color.my_dark_purple),
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Dirección del profesor
                IconWithText(
                    icon = Icons.Outlined.LocationOn,
                    text = stringResource(id = R.string.direction_info, professor.address, professor.postal),
                    iconSize = 30.dp,
                    textSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                // Teléfono del profesor
                IconWithText(
                    icon = Icons.Outlined.Phone,
                    text = professor.phone,
                    iconSize = 30.dp,
                    textSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                // Email del profesor
                IconWithText(
                    icon = Icons.Outlined.Email,
                    text = professor.email,
                    iconSize = 30.dp,
                    textSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

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

            // Columna contenedora de la información del anuncio
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        colorResource(id = R.color.my_dark_purple),
                        RoundedCornerShape(10.dp)
                    )
            ) {
                // Asignatura y precio del anuncio
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    // Asignatura
                    Text(
                        text = advert.subject,
                        color = colorResource(id = R.color.my_dark_gray),
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    // Precio
                    Text(
                        text = stringResource(
                            id = R.string.icon_euro_hour,
                            advert.price.toString()
                        ),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.my_dark_purple)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Descripción del anuncio
                IconWithText(
                    icon = Icons.Outlined.Description,
                    text = advert.description,
                    iconSize = 30.dp,
                    textSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Opciones de modalidad de clase
            val classModes = advert.classModes.split(", ").mapNotNull { value ->
                ClassMode.values().find { it.name == value }
            }.toSet()
            MultiOptionsSectionImmutable(
                title = stringResource(R.string.class_mode_advert),
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                options = listOf(
                    ClassMode.Presencial to ClassMode.Presencial.toString(),
                    ClassMode.Online to ClassMode.Online.toString(),
                    ClassMode.Hibrido to ClassMode.Hibrido.toString()
                ),
                selectedOptions = classModes
            )

            // Opciones de niveles de clase
            val levelsSet = advert.levels.split(", ").mapNotNull { value ->
                    Level.values().find { it.name == value }
                }.toSet()
            MultiOptionsSectionImmutable(
                title = stringResource(id = R.string.levels_advert),
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                options = listOf(
                    Level.Primaria to Level.Primaria.toString(),
                    Level.ESO to Level.ESO.toString(),
                    Level.Bachillerato to Level.Bachillerato.toString(),
                    Level.FP to Level.FP.toString(),
                    Level.Universidad to Level.Universidad.toString(),
                    Level.Adultos to Level.Adultos.toString()
                ),
                selectedOptions = levelsSet
            )
            Spacer(Modifier.height(20.dp))

            // Botón para solicitar una clase (versión tablet)
            if(windowSize == WindowWidthSizeClass.Expanded) {
                ButtonWithText(
                    label = stringResource(R.string.request_button_label),
                    buttonColor = colorResource(id = R.color.my_dark_purple),
                    textColor = Color.White,
                    onClick = { showDialog = true }
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

/**
 * Función que representa la pantalla de lista de anuncios y detalles de un anuncio.
 *
 * @param modifier Modificador opcional para aplicar al diseño.
 * @param onAdvertClick Función lambda que se invoca cuando se hace click en un anuncio de la lista.
 * @param onFavoriteButtonClick Función lambda que se ejecuta cuando se marca como favorito un anuncio.
 * @param onSendButtonClick Función lambda que se invoca cuando se hace click en el botón de enviar.
 * @param contentPadding Espaciado alrededor del contenido de la pantalla.
 * @param contentType Indica tipo de contenido de la pantalla (ListOnly o ListAndDetail).
 */
@Composable
fun AdvertsListAndDetail(
    windowSize: WindowWidthSizeClass,
    notFoundMessage: String,
    onQueryChange: (String) -> Unit,
    onAdvertClick: (Advert) -> Unit,
    onFavoriteButtonClick: (Advert) -> Unit,
    onRequestConfirm: (Request) -> Unit,
    onSendButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentType: AdvertsContentType = AdvertsContentType.ListOnly,
    advertsUiState: AdvertsUiState? = null,
    favoritesUiState: FavoritesUiState? = null
) {
    Row(
        modifier = modifier
    ) {
        // Representa la lista de anuncios
        AdvertsList(
            advertsUiState = advertsUiState,
            favoritesUiState = favoritesUiState,
            notFoundMessage = notFoundMessage,
            onQueryChange = onQueryChange,
            onAdvertClick = onAdvertClick,
            onFavoriteButtonClick = onFavoriteButtonClick,
            onSendButtonClick = onSendButtonClick,
            contentPadding = contentPadding,
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            contentType = contentType
        )

        // Obtener la información necesaria para mostrar detalles del anuncio
        val studentId = advertsUiState?.user?._id ?: favoritesUiState?.user?._id ?: ""
        val currentAdvert = advertsUiState?.currentAdvert ?: favoritesUiState?.currentAdvert ?: Advert()
        val professorsList = advertsUiState?.professorsList ?: favoritesUiState?.professorsList ?: emptyList()
        val professor = professorsList.find { it._id == currentAdvert.profId }
        professor?.let {
            // Representa los detalles de un anuncio específico
            AdvertDetail(
                windowSize = windowSize,
                studentId = studentId,
                advert = currentAdvert,
                professor = professor,
                onRequestConfirm = onRequestConfirm,
                modifier = Modifier.weight(3f),
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun IconWithText(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    textSize: TextUnit = 15.sp,
    iconColor: Color = colorResource(id = R.color.my_dark_gray),
    textColor: Color = colorResource(id = R.color.my_dark_gray)
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = modifier
                .size(iconSize)
                .padding(end = 4.dp)
        )
        Text(
            text = text,
            fontSize = textSize,
            color = textColor
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

/**
 * Función que previsualiza la lista de anuncios.
 */
@Preview
@Composable
fun AdvertItemPreview() {
    AdvertItem(
        professor = User(username = "Pepe"),
        advert = Advert(
            subject = "Lengua",
            price = 12,
            classModes = "Presencial, Hibrido",
            levels = "Bachillerato"
        ),
        isFavorite = true,
        isSelected = false,
        onAdvertClick = {},
        onFavoriteButtonClick = {},
        onSendButtonClick = {}
    )
}

@Preview
@Composable
fun AdvertDetailPreview() {
    AdvertDetail(
        windowSize = WindowWidthSizeClass.Medium,
        studentId = "",
        advert = Advert(
            subject = "Lengua",
            price = 12,
            classModes = "Presencial, Hibrido",
            levels = "Bachillerato",
            description = "Esto es una prueba de una descripción muy larga para ver" +
                    " como se comporta el diseño de la aplicación"
        ),
        professor = User(
            _id = "1",
            username = "Maria",
            phone = "657565343",
            address = "Calle Real",
            postal = "56474",
            email = "maria@gmail.com"
        ),
        onRequestConfirm = {}
    )
}