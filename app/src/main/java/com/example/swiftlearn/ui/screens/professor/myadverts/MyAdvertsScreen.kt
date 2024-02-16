package com.example.swiftlearn.ui.screens.professor.myadverts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ConfirmationDialog
import com.example.swiftlearn.ui.screens.student.IconWithText

@Composable
fun MyAdvertsScreen(
    navigateToEditAdvert: (String) -> Unit,
    onSendButtonClick: (String) -> Unit,
    viewModel: MyAdvertsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de mis anuncios
    val myAdvertsUiState = viewModel.myAdvertsUiState.collectAsState().value

    // Mostramos el icono cargando si está cargando
    if(myAdvertsUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        // Mostramos el formulario de perfil si no está cargando
    } else {
        Column {
            // Cabecera de mis anuncios
            MyAdvertsHeader()

            // Lista de anuncios del profesor
            MyAdvertsList(
                professor = myAdvertsUiState.user,
                advertsList = myAdvertsUiState.myAdvertsList,
                notFoundMessage = stringResource(R.string.not_adverts_yet),
                onEditButtonClick = {
                    navigateToEditAdvert(it._id)
                },
                onDeleteButtonClick = {
                    viewModel.deleteAdvert(it)
                },
                onSendButtonClick = onSendButtonClick
            )
        }
    }
}

/**
 * Función que representa la cabecera de la pantalla de mis anuncios.
 */
@Composable
private fun MyAdvertsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de nueva cuenta
        Text(
            text = stringResource(R.string.my_adverts_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun MyAdvertsList(
    professor: User,
    advertsList: List<Advert>,
    notFoundMessage: String,
    onEditButtonClick: (Advert) -> Unit,
    onDeleteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = modifier.height(20.dp))

        if (advertsList.isEmpty()) {
            // Mostrar mensaje cuando no se encuentran anuncios
            Text(
                text = notFoundMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            )
        } else {
            // Mostrar la lista de anuncios de ese profesor
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .padding(bottom = 62.dp)
                    .fillMaxHeight()
            ) {
                items(advertsList) { advert ->
                    MyAdvertItem(
                        professor = professor,
                        advert = advert,
                        onEditButtonClick = onEditButtonClick,
                        onDeleteButtonClick = onDeleteButtonClick,
                        onSendButtonClick = onSendButtonClick
                    )
                }
            }
        }

        // Agrega margen al final de la lista de anuncios
        Spacer(modifier = modifier.height(20.dp))
    }
}

@Composable
private fun MyAdvertItem(
    professor: User,
    advert: Advert,
    onEditButtonClick: (Advert) -> Unit,
    onDeleteButtonClick: (Advert) -> Unit,
    onSendButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado booleano para controlar si el diálogo de confirmación está abierto o no
    var showDialog by remember { mutableStateOf(false) }

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
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(id = R.color.my_light_pink))
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
                // Contenedor de los botones
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Contenedor del icono compartir
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = { onSendButtonClick(advertSummary) },
                        ) {
                            // Icono de compartir
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Outlined.Share,
                                tint = colorResource(id = R.color.my_dark_gray),
                                contentDescription = null
                            )
                        }
                    }

                    // Contenedor de los iconos de editar y eliminar
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono de editar
                        IconButton(
                            onClick = { onEditButtonClick(advert) }
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Outlined.Edit,
                                tint = colorResource(id = R.color.my_light_purple),
                                contentDescription = null
                            )
                        }

                        // Icono de eliminar
                        IconButton(
                            onClick = { showDialog = true }
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Filled.Delete,
                                tint = colorResource(id = R.color.my_red),
                                contentDescription = null
                            )
                        }
                    }
                }

                // Contenedor de asignatura y precio del anuncio
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

                // Contenedor de información de modos de clase
                IconWithText(
                    icon = Icons.Outlined.House,
                    text = advert.classModes,
                    iconSize = 20.dp,
                    textSize = 15.sp
                )

                // Contenedor de información de niveles de clase
                IconWithText(
                    icon = Icons.Outlined.Leaderboard,
                    text = advert.levels,
                    iconSize = 20.dp,
                    textSize = 15.sp
                )
            }

            // Mostramos el modal de confirmación si showDialog es true
            if (showDialog) {
                ConfirmationDialog(
                    title = stringResource(id = R.string.delete_advert_title),
                    textMessage = stringResource(id = R.string.sure_delete_advert_label),
                    onConfirm = {
                        // Si el usuario confirma, se llama a la función onDeleteClick
                        onDeleteButtonClick(advert)
                        // Se cierra el diálogo
                        showDialog = false
                    },
                    onCancel = {
                        // Si el usuario cancela, se cierra el diálogo
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MyAdvertItemPreview() {
    MyAdvertItem(
        professor = User(),
        advert = Advert(
            subject = "Matematicas",
            price = 15,
            classModes = "Presencial,Hibrido",
            levels = "Bachillerato"
        ),
        onEditButtonClick = {},
        onDeleteButtonClick = {},
        onSendButtonClick = {}
    )
}