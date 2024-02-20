package com.example.swiftlearn.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithTextAndImage
import com.example.swiftlearn.ui.components.ConfirmationDialog
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.screens.utils.ValidationUtils

/**
 * [ProfileScreen] define la pantalla de perfil de usuario.
 *
 * @param navigateToLogin Función de navegación para ir a la pantalla de inicio de sesión.
 * @param viewModel ViewModel para gestionar la pantalla de perfil de usuario.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navigateToLogin: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla del perfil
    val profileUiState = viewModel.profileUiState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    if(profileUiState.isLoading) {
        // Mostramos el icono cargando si está cargando
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostramos el formulario de perfil si no está cargando
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera de la pantalla
            ProfileHeader()

            // Formulario del perfil de usuario
            ProfileForm(
                profileUiState = profileUiState,
                onFieldChanged = viewModel::onFieldChanged,
                onSaveClick = {
                    viewModel.updateUser(it, context)
                },
                onDeleteClick = {
                    viewModel.deleteUser(it, navigateToLogin)
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

        // Texto de mi perfil
        Text(
            text = stringResource(R.string.profile_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

/**
 * Función que representa el formulario de perfil de usuario.
 *
 * @param profileUiState Estado de la interfaz de usuario.
 * @param onFieldChanged Función para manejar cambios en los campos del formulario.
 * @param onSaveClick Función para manejar evento click en el botón de guardar.
 * @param onDeleteClick Función para manejar evento click en el botón de eliminar.
 * @param onSignOutClick Función para manejar evento click en el botón de cerrar sesión.
 */
@Composable
private fun ProfileForm(
    profileUiState: ProfileUiState = ProfileUiState(),
    onFieldChanged: (ProfileDetails) -> Unit = {},
    onSaveClick: (User) -> Unit = {},
    onDeleteClick: (User) -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    // Variable para manejar la información del usuario
    val profileDetails = profileUiState.profileDetails

    // Estados booleano para controlar la muestra de los diálogos de confirmación
    var showDialogProfile by rememberSaveable { mutableStateOf(false) }
    var showDialogSession by rememberSaveable { mutableStateOf(false) }

    // Mostramos el modal de confirmación de eliminar perfil
    if (showDialogProfile) {
        ConfirmationDialog(
            title = stringResource(id = R.string.delete_profile_title),
            textMessage = stringResource(id = R.string.sure_delete_profile_label),
            onConfirm = {
                onDeleteClick(profileUiState.userLogged)
                showDialogProfile = false
            },
            onCancel = {
                showDialogProfile = false
            }
        )
    }

    // Mostramos el modal de confirmación de cerrar sesión
    if (showDialogSession) {
        ConfirmationDialog(
            title = stringResource(id = R.string.close_session_title),
            textMessage = stringResource(id = R.string.sure_close_session_label),
            onConfirm = {
                onSignOutClick()
                showDialogSession = false
            },
            onCancel = {
                showDialogSession = false
            }
        )
    }

    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Correo electrónico del usuario
        Text(
            text = profileUiState.userLogged.email,
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

        // Fila contenedora de los botones
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón Eliminar perfil
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_profile_label),
                    color = colorResource(R.color.my_dark_purple),
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color.White, CircleShape)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(R.color.my_dark_purple), CircleShape)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                showDialogProfile = true
                            }
                        )
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            // Botón Guardar
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.update_profile_label),
                    color = Color.White,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(40.dp)
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
                                onSaveClick(profileDetails.updateUser(profileUiState.userLogged))
                            }
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Botón para cerrar sesión
        ButtonWithTextAndImage(
            label = stringResource(R.string.sign_out_label),
            image = painterResource(R.drawable.icon_sign_out),
            buttonColor = Color.Transparent,
            borderButtonColor = Color.Transparent,
            textColor = colorResource(id = R.color.my_red),
            onClick = {
                showDialogSession = true
            }
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * [ProfileScreenPreview] es una función para previsualizar la pantalla de perfil de usuario.
 */
@Preview
@Composable
fun ProfileScreenPreview() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader()
        ProfileForm()
    }
}
