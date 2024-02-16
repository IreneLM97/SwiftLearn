package com.example.swiftlearn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Request
import com.example.swiftlearn.model.Status
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ConfirmationDialog(
    title: String,
    textMessage: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, fontSize = 20.sp)
            }
        },
        text = {
            Text(
                text = textMessage,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_gray)),
                modifier = Modifier.height(35.dp)
            ) {
                Text(text = stringResource(R.string.no), color = Color.White)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_red)),
                modifier = Modifier.height(35.dp)
            ) {
                Text(text = stringResource(R.string.yes), color = Color.White)
            }
        },
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = Color.Black,
        modifier = Modifier
            .border(3.dp, colorResource(id = R.color.my_red), shape = RoundedCornerShape(20.dp))
    )
}

@Composable
fun RequestClassDialog(
    windowSize: WindowWidthSizeClass,
    studentId: String,
    advertId: String,
    onRequestConfirm: (Request) -> Unit,
    onRequestCancel: () -> Unit
) {
    // Variable de estado para la fecha seleccionada
    val selectedDate = rememberSaveable { mutableStateOf(Calendar.getInstance()) }
    // Variable de estado para el formato de fecha
    val dateFormat = rememberSaveable { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Variables de estado para la hora seleccionada
    val selectedHour = rememberSaveable { mutableStateOf(8) }
    val selectedMinute = rememberSaveable { mutableStateOf(0) }

    // Determinamos el tamaño de las celdas del calendario en función del tamaño de la pantalla
    val cellSize = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 30.dp
        WindowWidthSizeClass.Expanded -> 60.dp
        else -> 30.dp
    }

    // Determinamos el tamaño del texto en función del tamaño de la pantalla
    val fontSize = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 14.sp
        WindowWidthSizeClass.Expanded -> 20.sp
        else -> 14.sp
    }

    // Creamos el diálogo
    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        text = {
            // Columna para organizar el contenido del diálogo verticalmente
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Mensaje de solicitar clase
                Text(
                    text = stringResource(R.string.request_class_label),
                    fontSize = fontSize,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Mostramos el calendario
                CalendarComponent(
                    cellSize = cellSize,
                    fontSize = fontSize,
                    selectedDate = selectedDate.value,
                    onDateSelected = { newDate ->
                        selectedDate.value = newDate },
                    onMonthChanged = { monthOffset ->
                        val newCalendar = selectedDate.value.clone() as Calendar
                        newCalendar.add(Calendar.MONTH, monthOffset)
                        selectedDate.value = newCalendar
                    }
                )

                // Mostramos selección de hora
                HourComponent(
                    cellSize = cellSize,
                    fontSize = fontSize,
                    selectedHour = selectedHour,
                    selectedMinute = selectedMinute
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Mostramos resumen de solicitud del alumno
                Text(
                    text = stringResource(
                        R.string.date_request_class,
                        dateFormat.format(selectedDate.value.time)
                    ),
                    fontSize = fontSize,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Mostramos la hora de la clase seleccionada
                Text(
                    text = stringResource(
                        R.string.hour_request_class,
                        String.format("%02d", selectedHour.value),
                        String.format("%02d", selectedMinute.value)
                    ),
                    fontSize = fontSize
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onRequestCancel,
                colors = ButtonDefaults.textButtonColors(Color.White),
                border = BorderStroke(2.dp, colorResource(id = R.color.my_dark_purple)),
                modifier = Modifier.height(35.dp)
            ) {
                Text(text = stringResource(R.string.cancel), color = colorResource(id = R.color.my_dark_purple))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val request = Request(
                        studentId = studentId,
                        advertId = advertId,
                        status = Status.Pendiente.toString(),
                        date = dateFormat.format(selectedDate.value.time),
                        hour = formatHourMinute(selectedHour.value, selectedMinute.value)
                    )
                    onRequestConfirm(request)
                },
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_dark_purple)),
                modifier = Modifier.height(35.dp)
            ) {
                Text(text = stringResource(R.string.request), color = Color.White)
            }
        },
        containerColor = colorResource(id = R.color.my_light_pink),
        titleContentColor = Color.Black,
        textContentColor = Color.Black
    )
}

@Composable
fun CalendarComponent(
    cellSize: Dp,
    fontSize: TextUnit,
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit,
    onMonthChanged: (Int) -> Unit
) {
    // Total de columnas para los días de la semana
    val totalColumns = 7
    // Total de celdas para 6 semanas (7 * 6)
    val totalCells = 42

    // Creamos instancia de Calendar que representa el mes y el año actuales
    val currentMonthCalendar = Calendar.getInstance()

    // Definimos el día de hoy
    val today = Calendar.getInstance()

    // Columna contenedora del calendario
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creamos una copia de selectedDate para realizar operaciones
        val calendar = selectedDate.clone() as Calendar
        // Configuramos el día del mes como el primer día del mes
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        // Obtenemos el número total de días en el mes
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        // Obtenemos el día de la semana del primer día del mes (esto es, Lunes .. Domingo)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)

        // Ajustamos el primer día para que el calendario empiece en Lunes
        val startDay = if (firstDayOfMonth == Calendar.SUNDAY) 7 else firstDayOfMonth - 1

        // Botones para navegar entre los meses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Habilitamos el botón de Anterior si no estamos en el mes y año actuales
            val enabled = calendar.get(Calendar.MONTH) != currentMonthCalendar.get(Calendar.MONTH) || calendar.get(Calendar.YEAR) != currentMonthCalendar.get(Calendar.YEAR)

            // Icono de Anterior
            IconButton(
                onClick = { if (enabled) onMonthChanged(-1) },
                enabled = enabled
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.previous_month_description))
            }
            // Mostramos mes actual
            Text(
                text = SimpleDateFormat(stringResource(R.string.mmmm_yyyy), Locale.getDefault()).format(calendar.time).uppercase(Locale.getDefault()),
                textAlign = TextAlign.Center,
                fontSize = fontSize,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )
            // Icono de Siguiente
            IconButton(
                onClick = { onMonthChanged(1) }
            ) {
                Icon(Icons.Filled.ArrowForward, contentDescription = stringResource(R.string.next_month_description))
            }
        }

        // Iniciales de los días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                stringResource(R.string.monday_initial),
                stringResource(R.string.tuesday_initial),
                stringResource(R.string.wednesday_initial),
                stringResource(R.string.thursday_initial),
                stringResource(R.string.friday_initial),
                stringResource(R.string.saturday_initial),
                stringResource(R.string.sunday_initial)
            ).forEach { initial ->
                Text(
                    text = initial,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    modifier = Modifier
                        .width(cellSize)
                        .padding(4.dp)
                )
            }
        }

        // Iterar sobre las celdas del calendario para mostrar los días del mes
        repeat(totalCells / totalColumns) { weekIndex ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Repetimos para cada día de la semana
                repeat(totalColumns) { dayIndex ->
                    // Calculamos el día correspondiente a la celda actual
                    val day = (weekIndex * totalColumns) + dayIndex - startDay + 2

                    // Verificamos si el día está dentro del mes actual
                    if (day in 1..daysInMonth) {
                        // Configuramos el día del mes como el dia sobre el que iteramos
                        calendar.set(Calendar.DAY_OF_MONTH, day)
                        // Verificamos si el día está seleccionado
                        val isSelected = day == selectedDate.get(Calendar.DAY_OF_MONTH)
                        // Verificamos si el día es futuro o es hoy
                        val isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                        val isFuture = calendar.timeInMillis > today.timeInMillis

                        // Renderizamos la celda del día
                        DayCell(
                            cellSize = cellSize,
                            fontSize = fontSize,
                            day = calendar.clone() as Calendar,
                            isSelected = isSelected,
                            isSelectable = isFuture || isToday,
                            isPastDate = !isFuture && !isToday,
                            onDateSelected = { onDateSelected(it) }
                        )
                    } else {
                        // Renderizamos celda vacía si el día no pertenece al mes actual
                        DayCell(
                            cellSize = cellSize,
                            fontSize = fontSize,
                            day = calendar.clone() as Calendar,
                            isSelected = false,
                            isSelectable = false,
                            isInMonth = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayCell(
    cellSize: Dp,
    fontSize: TextUnit,
    day: Calendar,
    isSelected: Boolean,
    isSelectable: Boolean = true,
    isPastDate: Boolean = false,
    isInMonth: Boolean = true,
    onDateSelected: (Calendar) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable(enabled = isSelectable, onClick = { if (isSelectable) onDateSelected(day) })
            .background(
                color =
                if (isSelected) colorResource(id = R.color.my_dark_purple)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .size(cellSize, cellSize),
        contentAlignment = Alignment.Center
    ) {
        // Escribimos el día solo si está en el mes actual
        if(isInMonth) {
            Text(
                text = day.get(Calendar.DAY_OF_MONTH).toString(),
                color =
                    if (isSelected) Color.White
                    else if (isPastDate) Color.Gray
                    else Color.Black,
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun HourComponent(
    cellSize: Dp,
    fontSize: TextUnit,
    selectedHour: MutableState<Int>,
    selectedMinute: MutableState<Int>
) {
    // Estado para manejar la expansión del menú de horas
    var isHourMenuExpanded by rememberSaveable { mutableStateOf(false) }
    // Estado para manejar la expansión del menú de minutos
    var isMinuteMenuExpanded by rememberSaveable { mutableStateOf(false) }

    // Selector de hora y minutos en una fila
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // Icono de reloj
        Icon(
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = null,
            tint = colorResource(id = R.color.my_dark_purple),
            modifier = Modifier
                .size(cellSize)
                .padding(horizontal = 4.dp)
        )

        // Selector de hora
        Column(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.my_dark_purple),
                    shape = RoundedCornerShape(8.dp)
                )
                .size(cellSize, cellSize)
                .clickable { isHourMenuExpanded = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto con la hora seleccionada
            Text(
                text = String.format("%02d", selectedHour.value),
                fontSize = fontSize,
                modifier = Modifier.padding(2.dp)
            )
            // Drowdown con las opciones de horas
            DropdownMenu(
                expanded = isHourMenuExpanded,
                onDismissRequest = { isHourMenuExpanded = false },
                modifier = Modifier.height(200.dp)
            ) {
                // Creamos elementos de menú para cada hora desde 8 hasta 22
                for (hour in 8..22) {
                    DropdownMenuItem(
                        onClick = {
                            selectedHour.value = hour
                            isHourMenuExpanded = false
                        },
                        text = { Text(String.format("%02d", hour)) }
                    )
                }
            }
        }

        // Separador de :
        Text(
            text = stringResource(id = R.string.hour_separator),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .padding(top = 5.dp)
        )

        // Selector de minutos
        Column(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.my_dark_purple),
                    shape = RoundedCornerShape(8.dp)
                )
                .size(cellSize, cellSize)
                .clickable { isMinuteMenuExpanded = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto con los minutos seleccionados
            Text(
                text = String.format("%02d", selectedMinute.value),
                fontSize = fontSize,
                modifier = Modifier.padding(2.dp)
            )
            // Drowdown con las opciones de minutos
            DropdownMenu(
                expanded = isMinuteMenuExpanded,
                onDismissRequest = { isMinuteMenuExpanded = false },
                modifier = Modifier.height(200.dp)
            ) {
                // Creamos elementos de menú para cada minuto en la lista [0, 15, 30, 45]
                for (minute in listOf(0, 15, 30, 45)) {
                    DropdownMenuItem(
                        onClick = {
                            selectedMinute.value = minute
                            isMinuteMenuExpanded = false
                        },
                        text = { Text(String.format("%02d", minute)) }
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

private fun formatHourMinute(hour: Int, minute: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

@Preview
@Composable
fun DeleteConfirmationDialogPreview() {
    ConfirmationDialog(
        title = stringResource(id = R.string.close_session_title),
        textMessage = stringResource(id = R.string.sure_close_session_label),
        onConfirm = {},
        onCancel = {}
    )
}

@Preview
@Composable
fun RequestClassDialogPreview() {
    RequestClassDialog(
        windowSize = WindowWidthSizeClass.Compact,
        studentId = "",
        advertId = "",
        onRequestConfirm = {},
        onRequestCancel = {}
    )
}
