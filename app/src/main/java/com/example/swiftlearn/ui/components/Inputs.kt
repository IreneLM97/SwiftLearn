package com.example.swiftlearn.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.swiftlearn.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun InputField(
    value: String,
    onValueChanged: (String) -> Unit = {},
    label: String,
    icon: ImageVector,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        interactionSource = interactionSource,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint =
                if (isFocused.value) colorResource(id = R.color.my_dark_purple)
                else colorResource(id = R.color.my_light_purple)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    password: String,
    passwordVisible: MutableState<Boolean>,
    onPasswordChanged: (String) -> Unit = {},
    icon: ImageVector,
    label: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val visualTransformation =
        if(passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(text = label) },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        trailingIcon = {
            PasswordVisibleIcon(
                passwordVisible = passwordVisible,
                tint =
                if (isFocused.value) colorResource(id = R.color.my_dark_purple)
                else colorResource(id = R.color.my_light_purple)

            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint =
                if (isFocused.value) colorResource(id = R.color.my_dark_purple)
                else colorResource(id = R.color.my_light_purple)
            )
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>,
    tint: Color
) {
    val image =
        if(passwordVisible.value) Icons.Default.VisibilityOff
        else Icons.Default.Visibility

    IconButton(
        onClick = { passwordVisible.value = !passwordVisible.value }
    ) {
        Icon(
            imageVector = image,
            contentDescription = stringResource(R.string.description_eye_icon),
            tint = tint
        )
    }
}
