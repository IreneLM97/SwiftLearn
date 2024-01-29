package com.example.swiftlearn.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftlearn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
// Diseño de la estructura básica de la pantalla
    Scaffold(
        topBar = {
            // Barra superior personalizada
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title),
                        color = colorResource(id = R.color.white)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(R.color.my_dark_purple)
                )
            )
        }
    ) { innerPadding ->
        // Dibujamos la cabecera del login
        LoginHeader(modifier.padding(innerPadding))
    }
}

/**
 * Función que representa la cabecera de la pantalla de login.
 */
@Composable
private fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Imagen que muestra la silueta de la ciudad
        Image(
            painter = painterResource(R.drawable.libro),
            contentDescription = stringResource(R.string.book_img),
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        // Texto de bienvenida
        Text(
            text = stringResource(R.string.welcome),
            color = colorResource(id = R.color.my_dark_purple),
            fontSize = 40.sp,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}