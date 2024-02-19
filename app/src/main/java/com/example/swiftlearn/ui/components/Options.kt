package com.example.swiftlearn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

/**
 * [Options] es una función para mostrar opciones seleccionables de selección única.
 *
 * @param options Lista de pares de opciones con sus etiquetas.
 * @param selectedOption Opción seleccionada actualmente.
 * @param onOptionSelected Función que se ejecuta cuando se selecciona una opción.
 */
@Composable
fun <T> Options(
    options: List<Pair<T, String>>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit
) {
    // Caja contenedora de las opciones
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fila contenedora de las opciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Recorremos las opciones
            options.forEach { (option, label) ->
                // Mostramos la opción con su etiqueta
                Surface(
                    border = BorderStroke(2.dp, colorResource(id = R.color.my_dark_purple)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 1.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOptionSelected(option) },
                    color = if (option == selectedOption) colorResource(id = R.color.my_dark_purple) else Color.White
                ) {
                    // Etiqueta de la opción
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = if (option == selectedOption) Color.White else Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * [MultiOptions] es una función para mostrar opciones seleccionables de selección múltiple.
 *
 * @param title Título que describe las opciones.
 * @param fontSize Tamaño de la fuente para el título.
 * @param fontStyle Estilo de la fuente para el título.
 * @param options Lista de pares de opciones con sus etiquetas.
 * @param selectedOptions Conjunto de opciones seleccionadas actualmente.
 * @param onOptionSelected Función que se ejecuta cuando se selecciona una opción.
 * @param isSelectable Indica si las opciones son seleccionables.
 */
@Composable
fun <T> MultiOptions(
    title: String = "",
    fontSize: TextUnit = 15.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    options: List<Pair<T, String>>,
    selectedOptions: Set<T>,
    onOptionSelected: (T) -> Unit = {},
    isSelectable: Boolean = true
) {
    // Columna contenedora de las opciones
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // Título descriptivo encima de las opciones
        Text(
            text = title,
            fontSize = fontSize,
            fontStyle =  fontStyle,
            color = colorResource(id = R.color.my_dark_gray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Dividir las opciones en filas de tres
        options.chunked(3).forEach { chunk ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                chunk.forEach { (option, label) ->
                    // Comprobamos si la opción está seleccionada
                    val isSelected = option in selectedOptions

                    // Mostramos la opción con su etiqueta
                    Surface(
                        border = BorderStroke(2.dp, colorResource(id = R.color.my_pink)),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) colorResource(id = R.color.my_pink) else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .padding(horizontal = 1.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (isSelectable) {
                                    onOptionSelected(option)
                                }
                            }
                    ) {
                        // Etiqueta de la opción
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * [MultiOptionsSection] es una función para mostrar una sección de múltiples opciones seleccionables.
 *
 * @param title Título para la sección de opciones.
 * @param options Lista de pares de opciones con sus etiquetas.
 * @param selectedOptions Conjunto de opciones seleccionadas actualmente.
 * @param onOptionSelected Función que se ejecuta cuando se selecciona una opción.
 */
@Composable
fun <T : Enum<T>> MultiOptionsSection(
    title: String,
    options: List<Pair<T, String>>,
    selectedOptions: Set<T>,
    onOptionSelected: (Set<T>) -> Unit = {}
) {
    // Estado mutable que almacena el conjunto de opciones seleccionadas
    val selectedOptionsState = remember { mutableStateOf(selectedOptions) }

    // Observamos el valor de selectedOptions y actualizamos selectedOptionsState
    LaunchedEffect(selectedOptions) {
        selectedOptionsState.value = selectedOptions
    }

    // Mostramos las múltiples opciones seleccionables
    MultiOptions(
        title = title,
        options = options,
        selectedOptions = selectedOptionsState.value,
        onOptionSelected = { option ->
            // Al pulsar una opción actualizamos las opciones seleccionadas
            val updatedOptions = selectedOptionsState.value.toMutableSet()
            if (option in updatedOptions) {
                updatedOptions.remove(option)  // si estaba seleccionada se elimina
            } else {
                updatedOptions.add(option)  // si no estaba seleccionada se añade
            }
            selectedOptionsState.value = updatedOptions

            onOptionSelected(updatedOptions)
        }
    )
}

/**
 * [MultiOptionsSectionImmutable] es una función para mostrar una sección de múltiples opciones no seleccionables.
 *
 * @param title Título para la sección de opciones.
 * @param fontSize Tamaño de fuente para el texto de las opciones.
 * @param fontStyle Estilo de fuente para el texto de las opciones.
 * @param options Lista de pares de opciones con sus etiquetas.
 * @param selectedOptions Conjunto de opciones seleccionadas actualmente.
 */
@Composable
fun <T : Enum<T>> MultiOptionsSectionImmutable(
    title: String = "",
    fontSize: TextUnit = 15.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    options: List<Pair<T, String>>,
    selectedOptions: Set<T>
) {
    // Mostramos las múltiples opciones no seleccionables
    MultiOptions(
        title = title,
        fontSize = fontSize,
        fontStyle = fontStyle,
        options = options,
        selectedOptions = selectedOptions,
        isSelectable = false
    )
}