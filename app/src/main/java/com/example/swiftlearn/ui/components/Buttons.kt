package com.example.swiftlearn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

/**
 * [ButtonWithText] es una función para crear un botón con texto en su interior.
 *
 * @param text Texto del botón.
 * @param buttonColor Color del fondo del botón.
 * @param borderButtonColor Color del borde del botón.
 * @param textColor Color del texto del botón.
 * @param isEnabled Indica si el botón está habilitado o no.
 * @param onClick Función que se ejecuta al hacer click en el botón.
 */
@Composable
fun ButtonWithText(
    text: String,
    buttonColor: Color,
    borderButtonColor: Color = Color.Transparent,
    textColor: Color,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    // Definimos el color del botón en función de si está habilitado o no
    val color = if (isEnabled) buttonColor else colorResource(id = R.color.my_gray_purple)

    // Creamos el botón con las características dadas
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .height(45.dp)
            .background(
                color = color,
                shape = CircleShape
            )
            .border(width = 2.dp, color = borderButtonColor, shape = CircleShape)
            .clip(shape = CircleShape)
            .fillMaxWidth(),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(color)
    ) {
        // Texto dentro del botón
        Text(
            text = text,
            style = TextStyle(color = textColor),
            fontSize = 20.sp
        )
    }
}

/**
 * [ButtonWithTextAndImage] es una función para crear un botón con texto e imagen en su interior.
 *
 * @param label Texto del botón.
 * @param image Imagen del botón.
 * @param buttonColor Color del fondo del botón.
 * @param borderButtonColor Color del borde del botón.
 * @param textColor Color del texto del botón.
 * @param onClick Función que se ejecuta al hacer click en el botón.
 */
@Composable
fun ButtonWithTextAndImage(
    label: String,
    image: Painter,
    buttonColor: Color,
    borderButtonColor: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    // Creamos el botón con las características dadas
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(45.dp)
            .background(
                color = buttonColor,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .border(
                width = 1.dp,
                color = borderButtonColor,
                shape = RoundedCornerShape(40.dp)
            )
            .fillMaxWidth(),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(buttonColor)
    ) {
        // Imagen dentro del botón
        Image(
            painter = image,
            contentDescription = stringResource(R.string.description_google_icon),
            modifier = Modifier
                .height(30.dp)
                .padding(end = 8.dp)
        )
        // Texto dentro del botón
        Text(
            text = label,
            style = TextStyle(color = textColor),
            fontSize = 20.sp
        )
    }
}

/**
 * [ToggleButton] es una función para crear un botón de alternancia (toggle button).
 *
 * @param isActivate Estado actual de la activación.
 * @param onToggleChecked Función que se ejecuta al cambiar el estado de activación.
 */
@Composable
fun ToggleButton(
    isActivate: Boolean = false,
    onToggleChecked: (Boolean) -> Unit = {}
) {
    // Fila contenedora del botón
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Creamos el botón de alternancia
        Switch(
            checked = isActivate,
            onCheckedChange = onToggleChecked,
            thumbContent =
            if (isActivate) {
                { Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(SwitchDefaults.IconSize)
                ) }
            } else {
                null
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Creamos un texto descriptivo para el botón
        Text(
            text = stringResource(R.string.remember_me_label),
            color = colorResource(id = R.color.my_dark_gray),
            fontSize = 17.sp
        )
    }
}

/**
 * [ButtonWithTextPreview] es una función para previsualizar un botón con texto.
 */
@Preview
@Composable
fun ButtonWithTextPreview() {
    ButtonWithText(
        text = stringResource(R.string.login_label),
        buttonColor = colorResource(id = R.color.my_dark_purple),
        textColor = colorResource(id = R.color.white),
        isEnabled = true,
        onClick = {}

    )
}

/**
 * [ButtonWithTextAndImagePreview] es una función para previsualizar un botón con texto e imagen.
 */
@Preview
@Composable
fun ButtonWithTextAndImagePreview() {
    ButtonWithTextAndImage(
        label = stringResource(id = R.string.google_label),
        image = painterResource(id = R.drawable.icon_google),
        buttonColor = colorResource(id = R.color.white),
        borderButtonColor = colorResource(id = R.color.my_dark_purple),
        textColor = colorResource(id = R.color.my_dark_purple),
        onClick = {}
    )
}