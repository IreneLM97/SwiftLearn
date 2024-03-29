package com.example.swiftlearn.ui.screens.professor.newadvert

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.components.MultiOptionsSection
import com.example.swiftlearn.ui.screens.utils.ValidationUtils

/**
 * [NewAdvertScreen] define la pantalla de crear nuevo anuncio.
 *
 * @param navigateToListAdverts Función de navegación para ir a la lista de anuncios.
 * @param viewModel ViewModel para gestionar la pantalla de nuevo anuncio.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewAdvertScreen(
    navigateToListAdverts: () -> Unit = {},
    viewModel: NewAdvertViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de nuevo anuncio
    val newAdvertUiState = viewModel.advertUiState.collectAsState().value

    if(newAdvertUiState.isLoading) {
        // Mostramos el icono cargando si está cargando
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostramos el formulario de nuevo anuncio si no está cargando
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera de la pantalla
            NewAdvertHeader()

            // Formulario de nuevo anuncio
            AdvertForm(
                advertUiState = newAdvertUiState,
                onFieldChanged = viewModel::onFieldChanged,
                onSaveClick = {
                    viewModel.insertAdvert(it)
                    navigateToListAdverts()
                }
            )
        }
    }
}

/**
 * Función que representa la cabecera de la pantalla de nuevo anuncio.
 */
@Composable
private fun NewAdvertHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de nuevo anuncio
        Text(
            text = stringResource(R.string.new_advert_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

/**
 * Función que representa el formulario de nuevo anuncio o editar anuncio.
 *
 * @param advertUiState Estado de la interfaz de usuario.
 * @param onFieldChanged Función para manejar cambios en los campos del formulario.
 * @param onSaveClick Función para manejar evento click en el botón de guardar.
 */
@Composable
fun AdvertForm(
    advertUiState: AdvertUiState,
    onFieldChanged: (AdvertDetails) -> Unit,
    onSaveClick: (Advert) -> Unit
) {
    // Variable para manejar la información del anuncio
    val advertDetails = advertUiState.advertDetails

    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo asignatura
        InputField(
            label = stringResource(R.string.subject_label),
            value = advertDetails.subject,
            onValueChange = { onFieldChanged(advertDetails.copy(subject = it.replace("\n", ""))) },
            isValid = advertDetails.subject.trim().isEmpty() || ValidationUtils.isSubjectValid(advertDetails.subject),
            errorMessage = stringResource(id = R.string.invalid_subject_label),
            leadingIcon = Icons.Outlined.MenuBook,
            keyboardType = KeyboardType.Text
        )

        // Campo precio
        InputField(
            label = stringResource(R.string.price_label),
            value =  if(advertDetails.price == 0) "" else advertDetails.price.toString(),
            onValueChange = { onFieldChanged(advertDetails.copy(price = it.replace("\n", "").toIntOrNull() ?: 0)) },
            leadingIcon = Icons.Filled.EuroSymbol,
            keyboardType = KeyboardType.Number
        )

        // Opciones de modo de clase
        MultiOptionsSection(
            title = stringResource(id = R.string.class_mode_label),
            options = listOf(
                ClassMode.Presencial to ClassMode.Presencial.toString(),
                ClassMode.Online to ClassMode.Online.toString(),
                ClassMode.Hibrido to ClassMode.Hibrido.toString()
            ),
            selectedOptions = advertDetails.classModes,
            onOptionSelected = {
                onFieldChanged(advertDetails.copy(classModes = it.toMutableSet()))
            }
        )

        // Opciones de niveles educativos
        MultiOptionsSection(
            title = stringResource(id = R.string.levels_label),
            options = listOf(
                Level.Primaria to Level.Primaria.toString(),
                Level.ESO to Level.ESO.toString(),
                Level.Bachillerato to Level.Bachillerato.toString(),
                Level.FP to Level.FP.toString(),
                Level.Universidad to Level.Universidad.toString(),
                Level.Adultos to Level.Adultos.toString()
            ),
            selectedOptions = advertDetails.levels,
            onOptionSelected = {
                onFieldChanged(advertDetails.copy(levels = it.toMutableSet()))
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Texto arriba de descripción
        Text(
            text = stringResource(id = R.string.description_text_label),
            fontSize = 15.sp,
            color = colorResource(id = R.color.my_dark_gray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Campo descripción
        InputField(
            label = stringResource(R.string.description_label),
            value =  advertDetails.description,
            onValueChange = { onFieldChanged(advertDetails.copy(description = it.replace("\n", ""))) },
            isValid = advertDetails.description.trim().isEmpty() || ValidationUtils.isDescriptionValid(advertDetails.description),
            errorMessage = stringResource(id = R.string.invalid_description_label),
            isSingleLine = false,
            leadingIcon = Icons.Default.Description,
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botón para guardar la información
        ButtonWithText(
            text = stringResource(R.string.save_advert_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = advertUiState.isEntryValid,
            onClick = {
                onSaveClick(advertDetails.toAdvert())
            }
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * Función para previsualizar la pantalla de crear nuevo anuncio.
 */
@Preview
@Composable
fun NewAdvertScreenPreview() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        NewAdvertHeader()
        AdvertForm(
            advertUiState = AdvertUiState(),
            onFieldChanged = {},
            onSaveClick = {}
        )
    }
}