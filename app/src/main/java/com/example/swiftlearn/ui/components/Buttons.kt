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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

@Composable
fun ButtonWithText(
    label: String,
    buttonColor: Color,
    textColor: Color,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val color = if (isEnabled) buttonColor else colorResource(id = R.color.my_gray_purple)

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .background(
                color = color,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .fillMaxWidth(),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(color = textColor),
            fontSize = 20.sp
        )
    }
}

@Composable
fun ButtonWithTextAndImage(
    label: String,
    image: Painter,
    buttonColor: Color,
    borderButtonColor: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
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
        Image(
            painter = image,
            contentDescription = stringResource(R.string.description_google_icon),
            modifier = Modifier
                .height(30.dp)
                .padding(end = 8.dp)
        )

        Text(
            text = label,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(color = textColor),
            fontSize = 20.sp
        )
    }
}

@Composable
fun ToggleButton(
    isActivate: Boolean = false,
    onToggleCkecked: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Switch(
            checked = isActivate,
            onCheckedChange = onToggleCkecked,
            thumbContent = if (isActivate) {
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
        Text(
            text = stringResource(R.string.remember_me_label),
            color = colorResource(id = R.color.my_dark_gray),
            fontSize = 17.sp
        )
    }
}