package com.example.swiftlearn.ui.screens.adverts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftlearn.ui.AppViewModelProvider

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AdvertScreen(
    viewModel: AdvertViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text("advert")
    }
}