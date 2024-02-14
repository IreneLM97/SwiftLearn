package com.example.swiftlearn.ui.screens.professor.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AccessTimeFilled
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.Status
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.screens.student.adverts.IconWithText

@Composable
fun ClassesScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Estado de selección para cada pestaña
        val selectedTab1 = rememberSaveable { mutableStateOf(true) }
        val selectedTab2 = rememberSaveable { mutableStateOf(false) }
        val selectedTab3 = rememberSaveable { mutableStateOf(false) }

        // TabRow para las pestañas
        TabRow(
            selectedTabIndex =
                if (selectedTab1.value) 0
                else if (selectedTab2.value) 1
                else 2,
            backgroundColor = Color.White,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = colorResource(id = R.color.my_dark_purple),
                    modifier = Modifier.tabIndicatorOffset(
                        when {
                            selectedTab1.value -> tabPositions[0]
                            selectedTab2.value -> tabPositions[1]
                            else -> tabPositions[2]
                        }
                    )
                )
            }
        ) {
            // Pestaña 1
            Tab(
                selected = selectedTab1.value,
                onClick = {
                    selectedTab1.value = true
                    selectedTab2.value = false
                    selectedTab3.value = false
                },
                text = {
                    Text(
                        text = Status.Pendiente.toString(),
                        fontSize =
                            if(selectedTab1.value) 17.sp
                            else 15.sp,
                        color =
                            if (selectedTab1.value) colorResource(id = R.color.my_dark_purple)
                            else colorResource(id = R.color.my_dark_gray),
                    )
                }
            )

            // Pestaña 2
            Tab(
                selected = selectedTab2.value,
                onClick = {
                    selectedTab1.value = false
                    selectedTab2.value = true
                    selectedTab3.value = false
                },
                text = {
                    Text(
                        text = Status.Confirmado.toString(),
                        fontSize =
                            if(selectedTab2.value) 17.sp
                            else 15.sp,
                        color =
                            if (selectedTab2.value) colorResource(id = R.color.my_dark_purple)
                            else colorResource(id = R.color.my_dark_gray)
                    )
                }
            )

            // Pestaña 3
            Tab(
                selected = selectedTab3.value,
                onClick = {
                    selectedTab1.value = false
                    selectedTab2.value = false
                    selectedTab3.value = true
                },
                text = {
                    Text(
                        text = Status.Denegado.toString(),
                        fontSize =
                            if(selectedTab3.value) 17.sp
                            else 15.sp,
                        color =
                            if (selectedTab3.value) colorResource(id = R.color.my_dark_purple)
                            else colorResource(id = R.color.my_dark_gray)
                    )
                }
            )
        }

        // Contenido de la pestaña seleccionada
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Aquí puedes mostrar el contenido de la pestaña seleccionada
        }
    }
}

@Composable
private fun ClassItem(
    student: User,
    advert: Advert,
    request: Request,
    onAcceptButtonClick: () -> Unit,
    onDeclineButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .background(colorResource(id = R.color.my_light_pink))
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.my_light_purple),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                )
        ) {
            // Datos del alumno
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
                Spacer(modifier = Modifier.height(10.dp))

                // Botón de Rechazar
                Text(
                    text = stringResource(R.string.decline_button_label),
                    color = colorResource(R.color.my_dark_gray),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(R.color.my_dark_gray), CircleShape)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable(onClick = onDeclineButtonClick)
                )
            }

            // Datos de la clase
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                // Estado de la petición
                IconWithText(
                    icon = Icons.Outlined.AccessTimeFilled,
                    text = Status.Pendiente.toString(),
                    iconColor = colorResource(id = R.color.my_yellow),
                    textColor = colorResource(id = R.color.my_yellow)
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Asignatura de la clase
                IconWithText(
                    icon = Icons.Outlined.MenuBook,
                    text = advert.subject
                )

                // Información de la fecha de la clase
                IconWithText(
                    icon = Icons.Outlined.DateRange,
                    text = request.date
                )

                // Información de la hora de la clase
                IconWithText(
                    icon = Icons.Outlined.AccessTime,
                    text = request.hour
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Botón de Aceptar
                Text(
                    text = stringResource(R.string.accept_button_label),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(colorResource(id = R.color.my_dark_purple), CircleShape)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(R.color.my_dark_purple), CircleShape)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable(onClick = onAcceptButtonClick)
                )
            }
        }
    }
}


@Preview
@Composable
fun ClassesScreenPreview() {
    ClassesScreen()
}

@Preview
@Composable
fun ClassItemPreview() {
    ClassItem(
        student = User(
            username = "Pepe",
            phone = "674534232",
            address = "Calle Real",
            postal = "54523",
            email = "correo@gmail.com"
        ),
        advert = Advert(
            subject = "Lengua",
            price = 12,
            classModes = "Presencial, Hibrido",
            levels = "Bachillerato"
        ),
        request = Request(
            status = "Pendiente",
            date = "10/03/2023",
            hour = "10:30"
        ),
        onAcceptButtonClick = {},
        onDeclineButtonClick = {}
    )
}