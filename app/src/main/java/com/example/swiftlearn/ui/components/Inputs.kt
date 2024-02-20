package com.example.swiftlearn.ui.components

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

/**
 * Función para crear un campo de texto de búsqueda.
 *
 * @param placeholder Texto descriptivo que se muestra cuando el campo está vacío.
 * @param query Valor actual del campo de búsqueda.
 * @param onQueryChange Función que se ejecuta al cambiar el valor del campo de búsqueda.
 * @param onSearch Función para manejar el evento de búsqueda.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    placeholder: String,
    query: String,
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    // Administrador del foco local
    val focusManager = LocalFocusManager.current

    // Creamos el campo de texto para ingresar la consulta de búsqueda
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            // Maneja el evento al pulsar el icono Search del teclado
            onSearch = {
                focusManager.clearFocus()
                onSearch(query)
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        modifier = Modifier
            .onKeyEvent { e ->
                // Maneja el evento al presionar la tecla "Enter" en el teclado
                if (e.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.clearFocus()
                    onSearch(query)
                }
                false
            }
            .fillMaxWidth()
            .height(52.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(40.dp)
            )
            .clip(RoundedCornerShape(40.dp))
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.my_dark_purple),
                shape = RoundedCornerShape(40.dp)
            )
    )
}

/**
 * Función para crear un campo de entrada genérico con opciones configurables.
 *
 * @param label Etiqueta que describe el campo.
 * @param value Valor actual del campo de entrada.
 * @param onValueChange Función que se ejecuta al cambiar el valor del campo.
 * @param isValid Indica si el valor actual del campo es válido.
 * @param errorMessage Mensaje de error que se muestra si el campo no es válido.
 * @param isSingleLine Indica si el campo debe ser de una sola línea.
 * @param leadingIcon Icono que se muestra antes del texto del campo.
 * @param keyboardType Tipo de teclado que se muestra al interactuar con el campo.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    isValid: Boolean = true,
    errorMessage: String? = null,
    isSingleLine: Boolean = true,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType
) {
    // Controlador del teclado local
    val keyboardController = LocalSoftwareKeyboardController.current

    // Administrador del foco local
    val focusManager = LocalFocusManager.current

    // Estado para verificar si el campo tiene el foco
    var isFocused by remember { mutableStateOf(false) }

    // Creamos el campo de entrada genérico
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .then(if (isSingleLine) Modifier else Modifier.height(150.dp))
            .onKeyEvent { e ->
                // Maneja el evento al presionar la tecla "Enter" en el teclado
                if (e.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                false
            }
            .onFocusChanged { isFocused = it.isFocused },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            // Maneja el evento al pulsar la tecla Done del teclado
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            if (isSingleLine) {
                Icon(imageVector = leadingIcon, contentDescription = null)
            } else {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(top = 15.dp)
                ) {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.my_dark_purple),
            focusedLabelColor = colorResource(id = R.color.my_dark_purple),
            focusedLeadingIconColor = colorResource(id = R.color.my_dark_purple),
            unfocusedBorderColor = if(isValid) colorResource(id = R.color.my_dark_gray) else Color.Red,
            unfocusedLabelColor = if(isValid) colorResource(id = R.color.my_dark_gray) else Color.Red,
            unfocusedLeadingIconColor = colorResource(id = R.color.my_light_purple)
        )
    )

    // Mostramos el mensaje de error si el campo no es válido y no está enfocado
    if (!isValid && !isFocused) {
        errorMessage?.let {
            ShowErrorMessage(errorMessage = it)
        }
    }
}

/**
 * Función para crear un campo de entrada de contraseña con opciones configurables.
 *
 * @param label Etiqueta que describe el campo.
 * @param password Contraseña actual.
 * @param passwordVisible Estado mutable que indica si la contraseña debe ser visible o no.
 * @param onPasswordChange Función que se ejecuta al cambiar el valor de la contraseña.
 * @param isValid Indica si la contraseña actual es válida.
 * @param errorMessage Mensaje de error que se muestra si la contraseña no es válida.
 * @param leadingIcon Icono que se muestra antes del campo de contraseña.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    label: String,
    password: String,
    passwordVisible: MutableState<Boolean>,
    onPasswordChange: (String) -> Unit = {},
    isValid: Boolean = true,
    errorMessage: String? = null,
    leadingIcon: ImageVector
) {
    // Controlador del teclado local
    val keyboardController = LocalSoftwareKeyboardController.current

    // Administrador del foco local
    val focusManager = LocalFocusManager.current

    // Estado para verificar si el campo tiene el foco
    var isFocused by remember { mutableStateOf(false) }

    // Permite determinar la visibilidad del campo contraseña
    val visualTransformation =
        if(passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation()

    // Creamos el campo de entrada de contraseña
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = label)
        },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .onKeyEvent { e ->
                // Maneja el evento al presionar la tecla "Enter" en el teclado
                if (e.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                false
            }
            .onFocusChanged { isFocused = it.isFocused },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            // Maneja el evento al pulsar la tecla Done del teclado
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        visualTransformation = visualTransformation,
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },
        trailingIcon = {
            PasswordVisibleIcon(passwordVisible = passwordVisible)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.my_dark_purple),
            focusedLabelColor = colorResource(id = R.color.my_dark_purple),
            focusedLeadingIconColor = colorResource(id = R.color.my_dark_purple),
            focusedTrailingIconColor = colorResource(id = R.color.my_dark_purple),
            unfocusedBorderColor = if(isValid) colorResource(id = R.color.my_dark_gray) else Color.Red,
            unfocusedLabelColor = if(isValid) colorResource(id = R.color.my_dark_gray) else Color.Red,
            unfocusedLeadingIconColor = colorResource(id = R.color.my_light_purple),
            unfocusedTrailingIconColor = colorResource(id = R.color.my_light_purple)
        )
    )

    // Mostramos el mensaje de error si el campo no es válido y no está enfocado
    if (!isValid && !isFocused) {
        errorMessage?.let {
            ShowErrorMessage(errorMessage = it)
        }
    }
}

/**
 * Función para crear un icono permite alternar la visibilidad de un campo contraseña.
 *
 * @param passwordVisible Estado mutable que indica si la contraseña debe ser visible o no.
 */
@Composable
private fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    // Determinamos qué icono mostrar en función de la visibilidad de la contraseña
    val image =
        if(passwordVisible.value) Icons.Default.VisibilityOff
        else Icons.Default.Visibility

    // Icono que cambia la visibilidad de la contraseña cuando se pulsa
    IconButton(
        onClick = { passwordVisible.value = !passwordVisible.value }
    ) {
        Icon(
            imageVector = image,
            contentDescription = stringResource(R.string.description_eye_icon)
        )
    }
}

/**
 * Función para mostrar un mensaje de error.
 *
 * @param errorMessage Mensaje de error que se quiere mostrar.
 */
@Composable
private fun ShowErrorMessage(
    errorMessage: String
) {
    // Creamos el texto con el mensaje a mostrar
    Text(
        text = errorMessage,
        color = Color.Red,
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}

/**
 * Función para previsualizar el campo de texto de búsqueda.
 */
@Preview
@Composable
fun SearchTextFieldPreview() {
    SearchTextField(
        placeholder = stringResource(id = R.string.search_label),
        query = ""
    )
}
