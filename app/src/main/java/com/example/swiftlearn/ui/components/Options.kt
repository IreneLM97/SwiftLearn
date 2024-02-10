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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

@Composable
fun <T> Options(
    options: List<Pair<T, String>>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { (option, label) ->
                Surface(
                    border = BorderStroke(2.dp, if (option == selectedOption) Color(0xFF9C27B0) else colorResource(id = R.color.my_dark_purple)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOptionSelected(option) },
                    color = if (option == selectedOption) colorResource(id = R.color.my_dark_purple) else Color.White
                ) {
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

@Composable
fun <T> MultiOptions(
    title: String = "",
    options: List<Pair<T, String>>,
    selectedOptions: Set<T>,
    onOptionSelected: (T) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // Texto arriba de las opciones
        Text(
            text = title,
            fontSize = 15.sp,
            color = colorResource(id = R.color.my_dark_gray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Dividir las opciones en filas de tres
        val chunkedOptions = options.chunked(3)

        chunkedOptions.forEach { chunk ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                chunk.forEach { (option, label) ->
                    val isSelected = option in selectedOptions

                    Surface(
                        border = BorderStroke(2.dp, if (isSelected) Color(0xFF9C27B0) else colorResource(id = R.color.my_dark_purple)),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) colorResource(id = R.color.my_dark_purple) else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onOptionSelected(option) }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = if (isSelected) Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
