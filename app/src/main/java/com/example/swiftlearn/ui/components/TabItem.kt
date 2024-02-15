package com.example.swiftlearn.ui.components

import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        text = {
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