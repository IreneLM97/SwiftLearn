package com.example.swiftlearn.ui.components

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R
import com.example.swiftlearn.model.Advert
import com.example.swiftlearn.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DeleteConfirmationModal(
    title: String,
    textMessage: String,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
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
            Text(text = textMessage, fontSize = 15.sp)
        },
        dismissButton = {
            TextButton(
                onClick = onDeleteCancel,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_gray))
            ) {
                Text(text = stringResource(R.string.no), color = Color.White)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteConfirm,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_red))
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
fun RequestClassModal(
    advert: Advert,
    professor: User?,
    onRequestConfirm: () -> Unit,
    onRequestCancel: () -> Unit
) {
    val selectedDate = remember { mutableStateOf(Calendar.getInstance()) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    AlertDialog(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
        onDismissRequest = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = stringResource(R.string.request_button_label), fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Profesor: ${professor?.username ?: ""}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Asignatura: ${advert.subject}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: ${advert.price}€",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedDate.value?.let { "Fecha seleccionada: ${dateFormat.format(it.time)}" }
                        ?: "Seleccione una fecha",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                CalendarComponent(
                    selectedDate = selectedDate.value,
                    onDateSelected = { newDate ->
                        selectedDate.value = newDate },
                    onMonthChanged = { monthOffset ->
                        val newCalendar = selectedDate.value.clone() as Calendar
                        newCalendar.add(Calendar.MONTH, monthOffset)
                        selectedDate.value = newCalendar
                    }
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onRequestCancel,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_gray))
            ) {
                Text(text = stringResource(R.string.cancel), color = Color.White)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onRequestConfirm,
                colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.my_dark_purple))
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
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit,
    onMonthChanged: (Int) -> Unit
) {
    val selectedHour = remember { mutableStateOf(10) }
    val selectedMinute = remember { mutableStateOf(30) }

    // Estado para manejar la expansión de los menús
    var isHourMenuExpanded by remember { mutableStateOf(false) }
    var isMinuteMenuExpanded by remember { mutableStateOf(false) }

    val totalColumns = 7 // Total de columnas para los días de la semana
    val currentMonthCalendar = Calendar.getInstance()

    // Definir 'today' dentro de 'CalendarComponent'
    val today = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val calendar = selectedDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Establecer el día en el primer día del mes
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)

        // Calcular el startDay considerando que el primer día de la semana sea el lunes
        val startDay = if (firstDayOfMonth == Calendar.SUNDAY) 7 else firstDayOfMonth - 1
        val totalCells = 42 // Total de celdas para 6 semanas

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    if (calendar.get(Calendar.MONTH) != currentMonthCalendar.get(Calendar.MONTH)) onMonthChanged(
                        -1
                    )
                },
                enabled = calendar.get(Calendar.MONTH) != currentMonthCalendar.get(Calendar.MONTH)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Mes anterior")
            }
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            IconButton(onClick = { onMonthChanged(1) }) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente mes")
            }
        }

        // Agregar las iniciales del día de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { initial ->
                Text(initial, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
            }
        }

        repeat(totalCells / totalColumns) { weekIndex ->
            Row(Modifier.fillMaxWidth()) {
                repeat(totalColumns) { dayIndex ->
                    val day = (weekIndex * totalColumns) + dayIndex - startDay + 2
                    if (day in 1..daysInMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, day)
                        val isSelected = day == selectedDate.get(Calendar.DAY_OF_MONTH)
                        val isFuture = calendar.timeInMillis > today.timeInMillis
                        val isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                        DayCell(
                            day = calendar.clone() as Calendar,
                            isSelected = isSelected,
                            isSelectable = isFuture || isToday,
                            isPastDate = !isFuture && !isToday,
                            onDateSelected = { onDateSelected(it) },
                        )
                    } else {
                        DayCell(
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

    // Selector de hora y minutos en una fila
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Selector de hora
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .background(Color.Red)
                .clickable { isHourMenuExpanded = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(String.format("%02d", selectedHour.value))
            // DropdownMenu para horas con altura máxima
            DropdownMenu(
                expanded = isHourMenuExpanded,
                onDismissRequest = { isHourMenuExpanded = false },
                modifier = Modifier
                    .height(200.dp)

            ) {
                for (hour in 10..22) {
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

        // Selector de minutos
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
                .clickable { isMinuteMenuExpanded = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(String.format("%02d", selectedMinute.value))

            // DropdownMenu para minutos con altura máxima
            DropdownMenu(
                expanded = isMinuteMenuExpanded,
                onDismissRequest = { isMinuteMenuExpanded = false },
                modifier = Modifier
                    .height(200.dp)

            ) {
                for (minute in listOf(0, 15, 30, 45)) {
                    DropdownMenuItem(
                        onClick = {
                            selectedMinute.value = minute
                            isMinuteMenuExpanded = false
                        },
                        text = { Text(String.format("%02d", minute)) } // Formato para asegurar dos dígitos
                    )
                }
            }
        }
    }




}


@Composable
fun DayCell(
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
                if (isSelected) Color.Gray
                else Color.Transparent
            )
            .size(30.dp), // Establecer el tamaño de la celda
        contentAlignment = Alignment.Center
    ) {
        if(isInMonth) {
            Text(
                text = day.get(Calendar.DAY_OF_MONTH).toString(),
                color =
                if (isSelected) Color.White
                else if (isPastDate) Color.Red
                else Color.Black
            )
        }
    }
}
