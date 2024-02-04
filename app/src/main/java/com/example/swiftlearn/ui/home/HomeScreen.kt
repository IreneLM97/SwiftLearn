package com.example.swiftlearn.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.login_title
}

@Composable
fun HomeScreen() {
    Text(text="hola")
}