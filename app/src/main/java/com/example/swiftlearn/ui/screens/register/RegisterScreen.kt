package com.example.swiftlearn.ui.screens.register

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.SwiftLearnTopAppBar
import com.example.swiftlearn.model.User
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.components.PasswordField
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.screens.ValidationUtils

object RegisterDestination : NavigationDestination {
    override val titleRes = R.string.register_app_bar
    override val route = "register"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToHome: () -> Unit = {},
    navigateBack: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de registro
    val registerUiState = viewModel.registerUiState.collectAsState().value
    // Guardamos el estado del mensaje de error
    val errorMessage = viewModel.errorMessage.collectAsState().value
    // Guardamos el estado de carga de la página
    val loadingState = viewModel.loadingState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    // Diseño de la estructura básica de la pantalla
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
        if(loadingState) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Cabecera del registro
                RegisterHeader()

                // Mostrar mensaje de error si existe
                errorMessage?.let {
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
                    onFieldChanged = { field, value ->
                        viewModel.onFieldChanged(field, value)
                    },
                    onRegisterClick = {
                        viewModel.createUserWithEmailAndPassword(context, it, navigateToHome)
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

@Composable
private fun RegisterForm(
    registerUiState: RegisterUiState = RegisterUiState(),
    onFieldChanged: (RegisterViewModel.Field, String) -> Unit = { _, _ -> },
    onRegisterClick: (User) -> Unit = { _ -> }
) {
    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Opciones de rol para que seleccione el usuario
        RolOptions(
            registerUiState = registerUiState,
            onRolSelected = {
                onFieldChanged(RegisterViewModel.Field.ROL, it.toString())
            }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Campo nombre de usuario
        InputField(
            label = stringResource(R.string.username_label),
            value =  registerUiState.usernameValue,
            onValueChange = { onFieldChanged(RegisterViewModel.Field.USERNAME, it) },
            leadingIcon = Icons.Default.AccountCircle,
            keyboardType = KeyboardType.Text
        )

        // Campo teléfono
        InputField(
            label = stringResource(R.string.phone_label),
            value =  registerUiState.phoneValue,
            onValueChange = { onFieldChanged(RegisterViewModel.Field.PHONE, it) },
            isValid = registerUiState.phoneValue.trim().isEmpty()
                    || ValidationUtils.isPhoneValid(registerUiState.phoneValue),
            errorMessage = stringResource(id = R.string.invalid_phone_label),
            leadingIcon = Icons.Filled.Phone,
            keyboardType = KeyboardType.Text
        )

        // Campo dirección
        InputField(
            label = stringResource(R.string.address_label),
            value =  registerUiState.addressValue,
            onValueChange = { onFieldChanged(RegisterViewModel.Field.ADDRESS, it) },
            leadingIcon = Icons.Default.LocationCity,
            keyboardType = KeyboardType.Text
        )

        // Campo código postal
        InputField(
            label = stringResource(R.string.postal_code_label),
            value =  registerUiState.postalValue,
            onValueChange = { onFieldChanged(RegisterViewModel.Field.POSTALCODE, it) },
            isValid = registerUiState.postalValue.trim().isEmpty()
                    || ValidationUtils.isPostalValid(registerUiState.postalValue),
            errorMessage = stringResource(id = R.string.invalid_postal_label),
            leadingIcon = Icons.Default.LocationOn,
            keyboardType = KeyboardType.Text
        )

        // Campo correo electrónico
        InputField(
            label = stringResource(R.string.email_label),
            value =  registerUiState.emailValue,
            onValueChange = { onFieldChanged(RegisterViewModel.Field.EMAIL, it) },
            isValid = registerUiState.emailValue.trim().isEmpty()
                    || ValidationUtils.isEmailValid(registerUiState.emailValue),
            errorMessage = stringResource(id = R.string.invalid_email_label),
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Text
        )

        // Campo contraseña
        PasswordField(
            label = stringResource(R.string.password_label),
            password = registerUiState.passwordValue,
            passwordVisible = rememberSaveable { mutableStateOf(false) },
            onPasswordChange = { onFieldChanged(RegisterViewModel.Field.PASSWORD, it) },
            isValid = registerUiState.passwordValue.trim().isEmpty()
                    || ValidationUtils.isPasswordValid(registerUiState.passwordValue),
            errorMessage = stringResource(id = R.string.invalid_password_label),
            leadingIcon = Icons.Default.Lock
        )

        // Campo repetir contraseña
        PasswordField(
            label = stringResource(R.string.confirm_password_label),
            password = registerUiState.confirmPasswordValue,
            passwordVisible = rememberSaveable { mutableStateOf(false) },
            onPasswordChange = { onFieldChanged(RegisterViewModel.Field.CONFIRMPASSWORD, it) },
            isValid = registerUiState.confirmPasswordValue.trim().isEmpty()
                    || ValidationUtils.isConfirmPasswordValid(registerUiState.passwordValue, registerUiState.confirmPasswordValue),
            errorMessage = stringResource(id = R.string.invalid_confirm_password_label),
            leadingIcon = Icons.Default.Repeat
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))

        // Botón para registrarse
        ButtonWithText(
            label = stringResource(R.string.register_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = RegisterViewModel.validateForm(registerUiState),
            onClick = {
                // Llama a la función createUserWithEmailAndPassword pasando el usuario
                onRegisterClick(
                    User(
                        id = "",
                        username = registerUiState.usernameValue,
                        phone = registerUiState.phoneValue,
                        address = registerUiState.addressValue,
                        postal = registerUiState.postalValue,
                        email = registerUiState.emailValue,
                        password = registerUiState.passwordValue,
                        rol = registerUiState.rolValue.toString()
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun RolOptions(
    registerUiState: RegisterUiState = RegisterUiState(),
    onRolSelected: (Rol) -> Unit = {}
) {
    // Contenedor de las dos opciones
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fila contenedora de las dos opciones
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Opción profesor
            Surface(
                border = BorderStroke(2.dp, colorResource(id = R.color.my_dark_purple)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onRolSelected(Rol.Profesor) },
                color =
                    if (registerUiState.rolValue == Rol.Profesor) colorResource(id = R.color.my_dark_purple)
                    else Color.White
            ) {
                Text(
                    text = stringResource(R.string.professor_label),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color =
                        if (registerUiState.rolValue == Rol.Profesor) Color.White
                        else Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Opción alumno
            Surface(
                border = BorderStroke(2.dp, Color(0xFF9C27B0)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onRolSelected(Rol.Alumno) },
                color =
                    if (registerUiState.rolValue == Rol.Alumno) colorResource(id = R.color.my_dark_purple)
                    else Color.White
            ) {
                Text(
                    text = stringResource(R.string.student_label),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color =
                        if (registerUiState.rolValue == Rol.Alumno) Color.White
                        else Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
