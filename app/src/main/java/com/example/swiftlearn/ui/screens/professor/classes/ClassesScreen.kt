package com.example.swiftlearn.ui.screens.professor.classes

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
import androidx.compose.material.Tab
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.Status
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.DeleteConfirmationDialog
import com.example.swiftlearn.ui.screens.student.IconWithText

@Composable
fun ClassesScreen(
    viewModel: ClassesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de clases
    val classesUiState = viewModel.classesUiState.collectAsState().value

    // Mostramos el icono cargando si está cargando
    if(classesUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    // Mostramos las solicitudes si no está cargando
    } else {
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
                onAcceptButtonClick = {
                    viewModel.updateRequest(it)
                    selectedTabIndex.value = 1
                },
                onDeclineButtonClick = {
                    viewModel.updateRequest(it)
                    selectedTabIndex.value = 2
                }
            )
        }
    }
}

@Composable
private fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        text = {
            Text(
                text = text,
                fontSize =
                if (isSelected) 17.sp
                else 15.sp,
                color =
                if (isSelected) colorResource(id = R.color.my_dark_purple)
                else colorResource(id = R.color.my_dark_gray),
            )
        }
    )
}

@Composable
private fun ClassesList(
    tabIndex: Int,
    classesUiState: ClassesUiState,
    onAcceptButtonClick: (Request) -> Unit,
    onDeclineButtonClick: (Request) -> Unit
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
                .height(670.dp)
        )
    } else {
        // Mostramos lista de solicitudes
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_small))
                .padding(bottom = 62.dp)
                .height(620.dp)
        ) {
            items(filteredRequests) { request ->
                // Identificamos el alumno vinculado a ese anuncio
                val student =
                    classesUiState.studentsList.find { it._id == request.studentId } ?: User()
                // Identificamos el anuncio vinculado a ese anuncio
                val advert =
                    classesUiState.advertsList.find { it._id == request.advertId } ?: Advert()

                // Mostramos la información de la solicitud
                ClassItem(
                    student = student,
                    advert = advert,
                    request = request,
                    onAcceptButtonClick = onAcceptButtonClick,
                    onDeclineButtonClick = onDeclineButtonClick
                )
            }
        }
    }
}

@Composable
private fun ClassItem(
    student: User,
    advert: Advert,
    request: Request,
    onAcceptButtonClick: (Request) -> Unit,
    onDeclineButtonClick: (Request) -> Unit,
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
        // Columna para datos del alumno y de la clase
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
            // Información del alumno
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                // Nombre del alumno
                Text(
                    text = student.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Información del teléfono
                IconWithText(
                    icon = Icons.Outlined.Phone,
                    text = student.phone
                )

                // Información del email
                IconWithText(
                    icon = Icons.Outlined.Email,
                    text = student.email
                )

                // Información de la dirección
                IconWithText(
                    icon = Icons.Outlined.LocationOn,
                    text = stringResource(
                        R.string.direction_info,
                        student.address,
                        student.postal
                    )
                )

                // Información de la clase
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Asignatura de la clase
                    Column(modifier = Modifier.weight(1.5f)) {
                        IconWithText(
                            icon = Icons.Outlined.MenuBook,
                            text = advert.subject
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

                // Botones de Aceptar y Rechazar solo si está en estado Pendiente
                if(request.status == Status.Pendiente.toString()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.decline_button_label),
                                color = colorResource(R.color.my_dark_gray),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .height(35.dp)
                                    .background(Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .border(2.dp, colorResource(R.color.my_dark_gray), CircleShape)
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable(onClick = { showDialog = true })
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.accept_button_label),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(
                                        colorResource(id = R.color.my_dark_purple),
                                        CircleShape
                                    )
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        colorResource(R.color.my_dark_purple),
                                        CircleShape
                                    )
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            onAcceptButtonClick(request.copy(status = Status.Aceptada.toString()))
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

    // Mostramos el modal de confirmación si showDialog es true
    if (showDialog) {
        DeleteConfirmationDialog(
            title = stringResource(id = R.string.decline_request_title),
            textMessage = stringResource(id = R.string.sure_decline_request_label),
            onDeleteConfirm = {
                onDeclineButtonClick(request.copy(status = Status.Rechazada.toString()))
                showDialog = false
            },
            onDeleteCancel = {
                showDialog = false
            }
        )
    }
}

@Preview
@Composable
fun ClassItemPreview() {
    val student = User(
        username = "Pepe",
        phone = "674534232",
        address = "Calle Real",
        postal = "54523",
        email = "correo@gmail.com"
    )

    val advert = Advert(
        subject = "Lengua",
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
            student = student,
            advert = advert,
            request = request,
            onAcceptButtonClick = {},
            onDeclineButtonClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClassItem(
            student = student,
            advert = advert,
            request = request.copy(status = Status.Aceptada.toString()),
            onAcceptButtonClick = {},
            onDeclineButtonClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClassItem(
            student = student,
            advert = advert,
            request = request.copy(status = Status.Rechazada.toString()),
            onAcceptButtonClick = {},
            onDeclineButtonClick = {}
        )
    }
}