package com.example.swiftlearn.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

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