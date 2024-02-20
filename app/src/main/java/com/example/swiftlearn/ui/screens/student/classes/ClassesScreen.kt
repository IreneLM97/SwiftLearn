package com.example.swiftlearn.ui.screens.student.classes

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.Status
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ConfirmationDialog
import com.example.swiftlearn.ui.components.TabItem
import com.example.swiftlearn.ui.screens.student.IconWithText
import kotlinx.coroutines.launch

/**
 * [ClassesScreen] define la pantalla de muestra de las solicitudes de clases.
 *
 * @param viewModel ViewModel para gestionar la pantalla de clases.
 */
@Composable
fun ClassesScreen(
    viewModel: ClassesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de clases
    val classesUiState = viewModel.classesUiState.collectAsState().value

    // Contexto de la aplicación
    val context = LocalContext.current

    // Variable para recordar el estado del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    // Ámbito del coroutine
    val coroutineScope = rememberCoroutineScope()

    // Estado booleano para controlar la muestra del Snackbar
    val showSnackbarCancelled = rememberSaveable { mutableStateOf(false) }

    // SnackbarHost para mostrar mensaje emergente
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .zIndex(2f)
            .padding(10.dp)
    )

    // LaunchedEffect para mostrar el Snackbar cuando se cancela una solicitud
    LaunchedEffect(showSnackbarCancelled.value) {
        if(showSnackbarCancelled.value) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_cancelled),
                    actionLabel = context.getString(R.string.snackbar_accept_button),
                    duration = SnackbarDuration.Short
                )
                showSnackbarCancelled.value = false
            }
        }
    }

    if(classesUiState.isLoading) {
        // Mostramos el icono cargando si está cargando
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostramos las solicitudes si no está cargando
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedTabIndex = rememberSaveable { mutableStateOf(0) }

            // TabRow para las pestañas
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = colorResource(id = R.color.my_dark_purple),
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value])
                    )
                }
            ) {
                // Pestaña Pendientes
                TabItem(
                    text = stringResource(R.string.pending_tab_label),
                    isSelected = selectedTabIndex.value == 0,
                    onClick = { selectedTabIndex.value = 0 }
                )

                // Pestaña Aceptadas
                TabItem(
                    text = stringResource(R.string.accepted_tab_label),
                    isSelected = selectedTabIndex.value == 1,
                    onClick = { selectedTabIndex.value = 1 }
                )

                // Pestaña Rechazadas
                TabItem(
                    text = stringResource(R.string.decline_tab_label),
                    isSelected = selectedTabIndex.value == 2,
                    onClick = { selectedTabIndex.value = 2 }
                )
            }

            // Contenido de la pestaña seleccionada
            ClassesList(
                tabIndex = selectedTabIndex.value,
                classesUiState = classesUiState,
                onCancelButtonClick = {
                    viewModel.deleteRequest(it)
                    showSnackbarCancelled.value = true
                }
            )
        }
    }
}

/**
 * Función que representa la lista de solicitudes de clases.
 *
 * @param tabIndex Indice de la pestaña seleccionada.
 * @param classesUiState Estado de la interfaz de usuario.
 * @param onCancelButtonClick Función que se ejecuta al pulsar el botón de cancelar.
 */
@Composable
private fun ClassesList(
    tabIndex: Int,
    classesUiState: ClassesUiState,
    onCancelButtonClick: (Request) -> Unit
) {
    // Filtramos la lista de solicitudes de clases en función del tab seleccionado
    val filteredRequests = when (tabIndex) {
        0 -> classesUiState.pendingRequests
        1 -> classesUiState.acceptedRequests
        2 -> classesUiState.deniedRequests
        else -> emptyList()
    }

    // Personalizamos mensaje de no encontrado si no hay solicitudes
    val notFoundMessage = when (tabIndex) {
        0 -> stringResource(R.string.not_found_pending)
        1 -> stringResource(R.string.not_found_accepted)
        2 -> stringResource(R.string.not_found_declined)
        else -> ""
    }

    if (filteredRequests.isEmpty()) {
        // Mostramos mensaje informativo si no hay solicitudes
        Text(
            text = notFoundMessage,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(700.dp)
        )
    } else {
        // Mostramos lista de solicitudes
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_small))
                .padding(bottom = 60.dp)
                .height(700.dp)
        ) {
            items(filteredRequests) { request ->
                // Identificamos el anuncio vinculado a esa petición
                val advert =
                    classesUiState.advertsList.find { it._id == request.advertId } ?: Advert()
                // Identificamos el profesor vinculado a ese anuncio
                val professor =
                    classesUiState.professorsList.find { it._id == advert.profId } ?: User()

                // Mostramos la información de la solicitud
                ClassItem(
                    professor = professor,
                    advert = advert,
                    request = request,
                    onCancelButtonClick = onCancelButtonClick
                )
            }
        }
    }
}

/**
 * Función que representa un elemento individual de la lista de solicitudes de clases.
 *
 * @param professor Profesor asociado al anuncio de la solicitud.
 * @param advert Anuncio asociado a la solicitud.
 * @param request Solicitud de clase.
 * @param onCancelButtonClick Función que se ejecuta al pulsar el botón de cancelar.
 * @param modifier Modificador de diseño.
 */
@Composable
private fun ClassItem(
    professor: User,
    advert: Advert,
    request: Request,
    onCancelButtonClick: (Request) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado booleano para controlar si el diálogo de confirmación está abierto o no
    var showDialog by remember { mutableStateOf(false) }

    // Diseño del item de la lista
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        // Fila contenedora de la información
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (request.status) {
                        Status.Pendiente.toString() -> colorResource(id = R.color.my_light_pink)
                        Status.Aceptada.toString() -> colorResource(id = R.color.my_light_green)
                        else -> colorResource(id = R.color.my_light_red)
                    }
                )
                .border(
                    width = 1.dp,
                    color = when (request.status) {
                        Status.Pendiente.toString() -> colorResource(id = R.color.my_dark_purple)
                        Status.Aceptada.toString() -> colorResource(id = R.color.my_dark_green)
                        else -> colorResource(id = R.color.my_dark_red)
                    },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                )
        ) {
            // Columna con información del profesor
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                // Nombre del profesor
                Text(
                    text = professor.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Teléfono del profesor
                IconWithText(
                    icon = Icons.Outlined.Phone,
                    text = professor.phone
                )

                // Correo electrónico del profesor
                IconWithText(
                    icon = Icons.Outlined.Email,
                    text = professor.email
                )

                // Dirección del profesor
                IconWithText(
                    icon = Icons.Outlined.LocationOn,
                    text = stringResource(
                        R.string.direction_info,
                        professor.address,
                        professor.postal
                    )
                )

                // Fila contenedora de la información de la clase
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Asignatura de la clase
                    Column(modifier = Modifier.weight(1.5f)) {
                        IconWithText(
                            icon = Icons.Outlined.MenuBook,
                            text = stringResource(
                                R.string.info_advert_request,
                                advert.subject,
                                advert.price
                            )
                        )
                    }
                    // Fecha de la clase
                    Column(modifier = Modifier.weight(1.5f)) {
                        IconWithText(
                            icon = Icons.Outlined.DateRange,
                            text = stringResource(
                                R.string.date_hour_request,
                                request.date,
                                request.hour
                            )
                        )
                    }
                }

                // Botón de cancelar solicitud si está en estado Pendiente
                if(request.status == Status.Pendiente.toString()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.cancel_button_label),
                                color = colorResource(R.color.my_dark_purple),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .height(35.dp)
                                    .background(Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        colorResource(R.color.my_dark_purple),
                                        CircleShape
                                    )
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable(onClick = { showDialog = true })
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
    }

    // Mostramos el modal de confirmación si showDialog es true
    if (showDialog) {
        ConfirmationDialog(
            title = stringResource(id = R.string.cancel_request_title),
            textMessage = stringResource(id = R.string.sure_cancel_request_label),
            onConfirm = {
                onCancelButtonClick(request)
                showDialog = false
            },
            onCancel = {
                showDialog = false
            }
        )
    }
}

/**
 * Función para previsualizar varios elementos de la lista de solicitudes de clases.
 */
@Preview
@Composable
fun ClassItemPreview() {
    val professor = User(
        username = "Laura",
        phone = "674534232",
        address = "Calle Real",
        postal = "54523",
        email = "correo@gmail.com"
    )

    val advert = Advert(
        subject = "Matematicas",
        price = 12,
        classModes = "Presencial, Hibrido",
        levels = "Bachillerato"
    )

    val request = Request(
        status = "Pendiente",
        date = "10/03/2023",
        hour = "10:30"
    )

    Column {
        ClassItem(
            professor = professor,
            advert = advert,
            request = request,
            onCancelButtonClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClassItem(
            professor = professor,
            advert = advert,
            request = request.copy(status = Status.Aceptada.toString()),
            onCancelButtonClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClassItem(
            professor = professor,
            advert = advert,
            request = request.copy(status = Status.Rechazada.toString()),
            onCancelButtonClick = {}
        )
    }
}