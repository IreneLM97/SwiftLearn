package com.example.swiftlearn.ui.components

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

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

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = isSingleLine,
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
            Icon(imageVector = leadingIcon, contentDescription = null)
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

    if (!isValid && !isFocused) {
        errorMessage?.let {
            ShowErrorMessage(errorMessage = it)
        }
    }
}

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

    if (!isValid && !isFocused) {
        errorMessage?.let {
            ShowErrorMessage(errorMessage = it)
        }
    }
}

@Composable
private fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if(passwordVisible.value) Icons.Default.VisibilityOff
        else Icons.Default.Visibility

    IconButton(
        onClick = { passwordVisible.value = !passwordVisible.value }
    ) {
        Icon(
            imageVector = image,
            contentDescription = stringResource(R.string.description_eye_icon)
        )
    }
}

@Composable
private fun ShowErrorMessage(
    errorMessage: String
) {
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
