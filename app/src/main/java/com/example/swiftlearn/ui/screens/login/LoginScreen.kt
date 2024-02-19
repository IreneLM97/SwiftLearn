package com.example.swiftlearn.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.R
import com.example.swiftlearn.SwiftLearnTopAppBar
import com.example.swiftlearn.ui.AppViewModelProvider
import com.example.swiftlearn.ui.components.ButtonWithText
import com.example.swiftlearn.ui.components.ButtonWithTextAndImage
import com.example.swiftlearn.ui.components.InputField
import com.example.swiftlearn.ui.components.PasswordField
import com.example.swiftlearn.ui.components.ToggleButton
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.screens.utils.ValidationUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

object LoginDestination : NavigationDestination {
    override val titleRes = R.string.login_title
    override val route = "login"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit = {},
    navigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Guardamos el estado de la pantalla de inicio de sesión
    val loginUiState = viewModel.loginUiState.collectAsState().value

    // Guardamos el contexto de la aplicación
    val context = LocalContext.current

    // Guardamos el token necesario para iniciar sesión con Google
    val token = "364363264567-74rs13s1fn9lk308blpml1mjhvjulgnp.apps.googleusercontent.com"

    // Creamos un launcher que se llamará para el inicio de sesión con Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential, navigateToHome)
        } catch(_: Exception) {}
    }

    // Diseño de la estructura básica de la pantalla
    Scaffold(
        topBar = {
            // Barra superior personalizada
            SwiftLearnTopAppBar(
                title = stringResource(id = LoginDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        if(loginUiState.isLoading) {
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
                // Cabecera del login
                LoginHeader()

                // Mostrar mensaje de error si existe
                loginUiState.errorMessage?.let {
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

                // Formulario de inicio de sesión
                LoginForm(
                    loginUiState = loginUiState,
                    onFieldChanged = viewModel::onFieldChanged,
                    onToggleChecked = viewModel::onToggleChanged,
                    onLoginClick = {
                        viewModel.signInWithEmailAndPassword(context, loginUiState.loginDetails, navigateToHome)
                    },
                    onGoogleLoginClick = {
                        val options = GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, options)
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                )

                // Mensaje para ir a Registrarse
                LoginToRegister(
                    onRegisterClick = navigateToRegister
                )
            }
        }
    }
}

/**
 * Función que representa la cabecera de la pantalla de login.
 */
@Composable
private fun LoginHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Imagen de un libro
        Image(
            painter = painterResource(R.drawable.icon_books),
            contentDescription = stringResource(R.string.description_book_icon),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de bienvenida
        Text(
            text = stringResource(R.string.welcome_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))
    }
}

@Composable
private fun LoginForm(
    loginUiState: LoginUiState = LoginUiState(),
    onFieldChanged: (LoginDetails) -> Unit = {},
    onToggleChecked: (Boolean) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {}
) {
    // Variable para manejar la información del usuario
    val loginDetails = loginUiState.loginDetails

    // Estructura del formulario
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo correo electrónico
        InputField(
            label = stringResource(R.string.email_label),
            value =  loginUiState.loginDetails.email,
            onValueChange = { onFieldChanged(loginDetails.copy(email = it.replace("\n", ""))) },
            isValid = loginDetails.email.trim().isEmpty() || ValidationUtils.isEmailValid(loginDetails.email),
            errorMessage = stringResource(R.string.invalid_email_label),
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Text
        )

        // Campo contraseña
        PasswordField(
            label = stringResource(R.string.password_label),
            password = loginUiState.loginDetails.password,
            passwordVisible = rememberSaveable { mutableStateOf(false) },
            onPasswordChange = { onFieldChanged(loginDetails.copy(password = it.replace("\n", ""))) },
            isValid = loginDetails.password.trim().isEmpty() || ValidationUtils.isPasswordValid(loginDetails.password),
            errorMessage = stringResource(id = R.string.invalid_password_label),
            leadingIcon = Icons.Default.Lock
        )

        // Toggle "Recuérdame"
        ToggleButton(
            isActivate = loginUiState.remember,
            onToggleChecked = onToggleChecked
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))

        // Botón para iniciar sesión
        ButtonWithText(
            text = stringResource(R.string.login_label),
            buttonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.white),
            isEnabled = loginUiState.isEntryValid,
            onClick = onLoginClick

        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))

        // Botón para iniciar sesión con Google
        ButtonWithTextAndImage(
            label = stringResource(id = R.string.google_label),
            image = painterResource(id = R.drawable.icon_google),
            buttonColor = colorResource(id = R.color.white),
            borderButtonColor = colorResource(id = R.color.my_dark_purple),
            textColor = colorResource(id = R.color.my_dark_purple),
            onClick = onGoogleLoginClick
        )
        Spacer(modifier = Modifier.height(35.dp))
    }
}

@Composable
private fun LoginToRegister(
    onRegisterClick: () -> Unit = {}
) {
    // Mensaje para registrarse si aún no tiene cuenta
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.no_account_label),
            color = colorResource(id = R.color.my_dark_gray),
            fontSize = 20.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(R.string.register_link_label),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 20.sp,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(5.dp)
        )
    }
    Spacer(modifier = Modifier.height(35.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreenPreview() {
     Scaffold(
        topBar = {
            SwiftLearnTopAppBar(
                title = stringResource(id = LoginDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            LoginHeader()
            LoginForm()
            LoginToRegister()
        }
    }
}