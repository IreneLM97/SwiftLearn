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
import com.example.swiftlearn.model.ClassMode
import com.example.swiftlearn.model.Level
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.components.MultiOptionsSection
import com.example.swiftlearn.ui.screens.utils.ValidationUtils

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewAdvertScreen(
    navigateToListAdverts: () -> Unit = {},
    viewModel: NewAdvertViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de nuevo anuncio
    val newAdvertUiState = viewModel.advertUiState.collectAsState().value

    // Mostramos el icono cargando si está cargando
    if(newAdvertUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    // Mostramos el formulario de nuevo anuncio si no está cargando
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera de la pantalla
            NewAdvertHeader()

            // Formulario de anuncio
            AdvertForm(
                advertUiState = newAdvertUiState,
                onFieldChanged = viewModel::onFieldChanged,
                onSaveClick = {
                    viewModel.insertAdvert(newAdvertUiState.advertDetails.toAdvert())
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

        // Texto de nueva cuenta
        Text(
            text = stringResource(R.string.new_advert_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

@Composable
fun AdvertForm(
    advertUiState: AdvertUiState,
    onFieldChanged: (AdvertDetails) -> Unit,
    onSaveClick: () -> Unit
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
                ClassMode.Presencial to stringResource(id = R.string.presencial_label),
                ClassMode.Online to stringResource(id = R.string.online_label),
                ClassMode.Hibrido to stringResource(id = R.string.hibrido_label)
            ),
            selectedOptions = advertDetails.classModes,
            onOptionSelected = {
                onFieldChanged(advertDetails.copy(classModes = it.toMutableSet()))
            }
        )

        // Opciones de niveles
        MultiOptionsSection(
            title = stringResource(id = R.string.levels_label),
            options = listOf(
                Level.Primaria to stringResource(id = R.string.primaria_label),
                Level.ESO to stringResource(id = R.string.eso_label),
                Level.Bachillerato to stringResource(id = R.string.bachillerato_label),
                Level.FP to stringResource(id = R.string.fp_label),
                Level.Universidad to stringResource(id = R.string.universidad_label),
                Level.Adultos to stringResource(id = R.string.adultos_label)
            ),
            selectedOptions = advertDetails.levels,
            onOptionSelected = {
                onFieldChanged(advertDetails.copy(levels = it.toMutableSet()))
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Text arriba de descripción
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
            label = stringResource(R.string.save_advert_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = advertUiState.isEntryValid,
            onClick = onSaveClick
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

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