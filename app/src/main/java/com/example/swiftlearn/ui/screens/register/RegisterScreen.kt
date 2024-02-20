package com.example.swiftlearn.ui.screens.register

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.swiftlearn.SwiftLearnTopAppBar
import com.example.swiftlearn.model.Role
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.components.Options
import com.example.swiftlearn.ui.components.PasswordField
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.screens.utils.ValidationUtils

/**
 * Destino de navegación para la pantalla de registro.
 */
object RegisterDestination : NavigationDestination {
    override val titleRes = R.string.register_app_bar
    override val route = "register"
}

/**
 * [RegisterScreen] define la pantalla de registro de usuario.
 *
 * @param navigateToHome Función de navegación para ir a la pantalla principal.
 * @param navigateBack Función de navegación hacia atrás.
 * @param viewModel ViewModel para gestionar la pantalla de registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToHome: () -> Unit = {},
    navigateBack: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de registro
    val registerUiState = viewModel.registerUiState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    // Diseño de la pantalla
    Scaffold(
        topBar = {
            // Barra superior personalizada
            SwiftLearnTopAppBar(
                title = stringResource(id = RegisterDestination.titleRes),
                canNavigateBack = true,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        if(registerUiState.isLoading) {
            // Mostramos el icono cargando si está cargando
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Mostramos formulario de registro si no está cargando
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Cabecera de la pantalla
                RegisterHeader()

                // Mostramos mensaje de error si ya existe esa cuenta
                registerUiState.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                }

                // Formulario de registro
                RegisterForm(
                    registerUiState = registerUiState,
                    onFieldChanged = viewModel::onFieldChanged,
                    onRegisterClick = {
                        viewModel.createUserWithEmailAndPassword(context, registerUiState.registerDetails, navigateToHome)
                    }
                )
            }
        }
    }
}

/**
 * Función que representa la cabecera de la pantalla de registro.
 */
@Composable
private fun RegisterHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de nueva cuenta
        Text(
            text = stringResource(R.string.new_account_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

/**
 * Función que representa el formulario de registro.
 *
 * @param registerUiState Estado de la interfaz de usuario.
 * @param onFieldChanged Función para manejar cambios en los campos del formulario.
 * @param onRegisterClick Función para manejar el evento de registrarse.
 */
@Composable
private fun RegisterForm(
    registerUiState: RegisterUiState = RegisterUiState(),
    onFieldChanged: (RegisterDetails) -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    // Variable para manejar la información del usuario
    val registerDetails = registerUiState.registerDetails

    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Opciones de role para que seleccione el usuario
        RoleOptions(
            registerDetails = registerDetails,
            onRoleSelected = { onFieldChanged(registerDetails.copy(role = it)) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Campo nombre de usuario
        InputField(
            label = stringResource(R.string.username_label),
            value = registerDetails.username,
            onValueChange = { onFieldChanged(registerDetails.copy(username = it.replace("\n", ""))) },
            leadingIcon = Icons.Default.AccountCircle,
            keyboardType = KeyboardType.Text
        )

        // Campo teléfono
        InputField(
            label = stringResource(R.string.phone_label),
            value =  registerDetails.phone,
            onValueChange = { onFieldChanged(registerDetails.copy(phone = it.replace("\n", ""))) },
            isValid = registerDetails.phone.trim().isEmpty() || ValidationUtils.isPhoneValid(registerDetails.phone),
            errorMessage = stringResource(id = R.string.invalid_phone_label),
            leadingIcon = Icons.Filled.Phone,
            keyboardType = KeyboardType.Text
        )

        // Campo dirección
        InputField(
            label = stringResource(R.string.address_label),
            value =  registerDetails.address,
            onValueChange = { onFieldChanged(registerDetails.copy(address = it.replace("\n", ""))) },
            leadingIcon = Icons.Default.LocationCity,
            keyboardType = KeyboardType.Text
        )

        // Campo código postal
        InputField(
            label = stringResource(R.string.postal_code_label),
            value =  registerDetails.postal,
            onValueChange = { onFieldChanged(registerDetails.copy(postal = it.replace("\n", ""))) },
            isValid = registerDetails.postal.trim().isEmpty()
                    || ValidationUtils.isPostalValid(registerDetails.postal),
            errorMessage = stringResource(id = R.string.invalid_postal_label),
            leadingIcon = Icons.Default.LocationOn,
            keyboardType = KeyboardType.Text
        )

        // Campo correo electrónico
        InputField(
            label = stringResource(R.string.email_label),
            value =  registerDetails.email,
            onValueChange = { onFieldChanged(registerDetails.copy(email = it.replace("\n", ""))) },
            isValid = registerDetails.email.trim().isEmpty()
                    || ValidationUtils.isEmailValid(registerDetails.email),
            errorMessage = stringResource(id = R.string.invalid_email_label),
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Text
        )

        // Campo contraseña
        PasswordField(
            label = stringResource(R.string.password_label),
            password = registerDetails.password,
            passwordVisible = rememberSaveable { mutableStateOf(false) },
            onPasswordChange = { onFieldChanged(registerDetails.copy(password = it.replace("\n", ""))) },
            isValid = registerDetails.password.trim().isEmpty()
                    || ValidationUtils.isPasswordValid(registerDetails.password),
            errorMessage = stringResource(id = R.string.invalid_password_label),
            leadingIcon = Icons.Default.Lock
        )

        // Campo repetir contraseña
        PasswordField(
            label = stringResource(R.string.confirm_password_label),
            password = registerDetails.confirmPassword,
            passwordVisible = rememberSaveable { mutableStateOf(false) },
            onPasswordChange = { onFieldChanged(registerDetails.copy(confirmPassword = it.replace("\n", ""))) },
            isValid = registerDetails.confirmPassword.trim().isEmpty()
                    || ValidationUtils.isConfirmPasswordValid(registerDetails.password, registerDetails.confirmPassword),
            errorMessage = stringResource(id = R.string.invalid_confirm_password_label),
            leadingIcon = Icons.Default.Repeat
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))

        // Botón para registrarse
        ButtonWithText(
            text = stringResource(R.string.register_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = registerUiState.isEntryValid,
            onClick = onRegisterClick
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * Función para mostrar opciones de roles para el registro.
 *
 * @param registerDetails Detalles del registro.
 * @param onRoleSelected Función que se ejecuta al seleccionar un rol.
 */
@Composable
private fun RoleOptions(
    registerDetails: RegisterDetails = RegisterDetails(),
    onRoleSelected: (Role) -> Unit = {}
) {
    // Creamos las opciones con los distintos roles
    Options(
        options = listOf(
            Role.Alumno to Role.Alumno.toString(),
            Role.Profesor to Role.Profesor.toString()
        ),
        selectedOption = registerDetails.role,
        onOptionSelected = onRoleSelected
    )
}

/**
 * Función para previsualizar la pantalla de registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegisterScreenPreview() {
    Scaffold(
        topBar = {
            SwiftLearnTopAppBar(
                title = stringResource(id = RegisterDestination.titleRes),
                canNavigateBack = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            RegisterHeader()
            RegisterForm()
        }
    }
}
