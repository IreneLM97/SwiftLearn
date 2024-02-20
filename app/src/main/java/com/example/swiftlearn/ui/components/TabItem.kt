package com.example.swiftlearn.ui.components

import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

/**
 * Función para crear una pestaña de un menú de pestañas.
 *
 * @param text Texto a mostrar en la pestaña.
 * @param isSelected Indica si la pestaña está seleccionada.
 * @param onClick Función que se ejecuta cuando se hace click en la pestaña.
 */
@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Creamos la pestaña
    Tab(
        selected = isSelected,
        onClick = onClick,
        text = {
            // Texto descriptivo de la pestaña
            Text(
                text = text,
                fontSize =
                if (isSelected) 17.sp
                else 15.sp,
                color =
                if (isSelected) colorResource(id = R.color.my_dark_purple)
                else colorResource(id = R.color.my_dark_gray),
            )
        }
    )
}