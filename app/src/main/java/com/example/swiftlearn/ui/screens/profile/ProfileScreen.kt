package com.example.swiftlearn.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.ButtonWithTextAndImage
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.screens.ValidationUtils

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navigateToLogin: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla del perfil
    val profileUiState = viewModel.profileUiState.collectAsState().value

    // Mostramos el icono cargando si está cargando
    if(profileUiState.loadingState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    // Mostramos el formulario de perfil si no está cargando
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera del perfil
            ProfileHeader()

            // Formulario del perfil
            ProfileForm(
                profileUiState = profileUiState,
                onFieldChanged = viewModel::onFieldChanged,
                onSaveClick = { viewModel.updateUser(profileUiState.profileDetails.toUser()) },
                onDeleteClick = {
                    viewModel.deleteUser(profileUiState.profileDetails.toUser())
                    navigateToLogin()
                },
                onSignOutClick = {
                    viewModel.signOut()
                    navigateToLogin()
                }
            )
        }
    }
}

/**
 * Función que representa la cabecera de la pantalla de perfil.
 */
@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de nueva cuenta
        Text(
            text = stringResource(R.string.profile_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

@Composable
private fun ProfileForm(
    profileUiState: ProfileUiState,
    onFieldChanged: (ProfileDetails) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    // Variable para manejar la información del usuario
    val profileDetails = profileUiState.profileDetails

    // Estado booleano para controlar si el diálogo de confirmación está abierto o no
    var showDialog by remember { mutableStateOf(false) }

    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email del usuario
        Text(
            text = profileDetails.email,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Campo nombre de usuario
        InputField(
            label = stringResource(R.string.username_label),
            value = profileDetails.username,
            onValueChange = { onFieldChanged(profileDetails.copy(username = it.replace("\n", ""))) },
            leadingIcon = Icons.Default.AccountCircle,
            keyboardType = KeyboardType.Text
        )

        // Campo teléfono
        InputField(
            label = stringResource(R.string.phone_label),
            value =  profileDetails.phone,
            onValueChange = { onFieldChanged(profileDetails.copy(phone = it.replace("\n", ""))) },
            isValid = profileDetails.phone.trim().isEmpty() || ValidationUtils.isPhoneValid(profileDetails.phone),
            errorMessage = stringResource(id = R.string.invalid_phone_label),
            leadingIcon = Icons.Filled.Phone,
            keyboardType = KeyboardType.Text
        )

        // Campo dirección
        InputField(
            label = stringResource(R.string.address_label),
            value =  profileDetails.address,
            onValueChange = { onFieldChanged(profileDetails.copy(address = it.replace("\n", ""))) },
            leadingIcon = Icons.Default.LocationCity,
            keyboardType = KeyboardType.Text
        )

        // Campo código postal
        InputField(
            label = stringResource(R.string.postal_code_label),
            value =  profileDetails.postal,
            onValueChange = { onFieldChanged(profileDetails.copy(postal = it.replace("\n", ""))) },
            isValid = profileDetails.postal.trim().isEmpty() || ValidationUtils.isPostalValid(profileDetails.postal),
            errorMessage = stringResource(id = R.string.invalid_postal_label),
            leadingIcon = Icons.Default.LocationOn,
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Botón para guardar la información
        ButtonWithText(
            label = stringResource(R.string.update_account_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = profileUiState.isEntryValid,
            onClick = onSaveClick
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botón para eliminar la cuenta
        ButtonWithText(
            label = stringResource(R.string.delete_account_label),
            buttonColor = colorResource(id = R.color.my_red),
            textColor = colorResource(id = R.color.white),
            onClick = { showDialog = true }
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Botón para cerrar sesión
        ButtonWithTextAndImage(
            label = stringResource(R.string.sign_out_label),
            image = painterResource(R.drawable.icon_sign_out),
            buttonColor = Color.White,
            borderButtonColor = colorResource(id = R.color.my_red),
            textColor = colorResource(id = R.color.my_red),
            onClick = onSignOutClick
        )
        Spacer(modifier = Modifier.height(100.dp))

        // Mostramos el modal de confirmación si showDialog es true
        if (showDialog) {
            DeleteConfirmationModal(
                onDeleteConfirm = {
                    // Si el usuario confirma, se llama a la función onDeleteClick
                    onDeleteClick()
                    // Se cierra el diálogo
                    showDialog = false
                },
                onDeleteCancel = {
                    // Si el usuario cancela, se cierra el diálogo
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun DeleteConfirmationModal(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_dangerous),
                    contentDescription = null,
                    tint = colorResource(id = R.color.my_red),
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.delete_modal_label),
                    fontSize = 20.sp
                )
            }
        },
        text = {
            Text(text = stringResource(R.string.sure_label), fontSize = 15.sp)
        },
        dismissButton = {
            TextButton(
                onClick = onDeleteCancel,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_gray))
            ) {
                Text(text = stringResource(R.string.no), color = Color.White)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteConfirm,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_red))
            ) {
                Text(text = stringResource(R.string.yes), color = Color.White)
            }
        },
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = Color.Black,
        modifier = Modifier
            .border(3.dp, colorResource(id = R.color.my_red), shape = RoundedCornerShape(20.dp))
    )
}