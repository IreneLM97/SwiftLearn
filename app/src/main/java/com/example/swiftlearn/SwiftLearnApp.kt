package com.example.swiftlearn

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.swiftlearn.ui.navigation.SwiftLearnNavigation

/** [SwiftLearnApp] define el punto de entrada principal de la aplicación SwiftLearn.
 *
 * @param windowSize Tamaño de la ventana donde se está mostrando la aplicación.
 * @param navController Controlador de navegación.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SwiftLearnApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController()
) {
    SwiftLearnNavigation(windowSize = windowSize, navController = navController)
}

/** Función para definir la barra superior de la aplicación SwiftLearn.
 *
 * @param title Título que se mostrará en la barra de la aplicación.
 * @param canNavigateBack Indica si la pantalla es navegable hacia atrás.
 * @param modifier Modificador de diseño.
 * @param scrollBehavior Comportamiento de desplazamiento de la barra superior.
 * @param navigateBack Función de navegación hacia atrás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwiftLearnTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateBack: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white)
            )
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(R.color.my_dark_purple)
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(R.string.description_back_icon),
                        tint = Color.White
                    )
                }
            }
        }
    )
}


