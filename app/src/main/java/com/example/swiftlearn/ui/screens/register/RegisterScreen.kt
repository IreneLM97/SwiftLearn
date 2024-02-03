package com.example.swiftlearn.ui.screens.register

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.navigation.NavigationDestination

object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.register
}

@Composable
fun RegisterScreen() {
    Text(
        text = "hola"
    )
}