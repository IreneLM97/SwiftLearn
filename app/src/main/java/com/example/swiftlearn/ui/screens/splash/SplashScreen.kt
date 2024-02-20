package com.example.swiftlearn.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.navigation.NavigationDestination
import com.example.swiftlearn.ui.screens.home.HomeDestination
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

/**
 * Destino de navegación para la pantalla splash de arranque.
 */
object SplashDestination : NavigationDestination {
    override val titleRes = null
    override val route = "splash"
}

/**
 * [SplashScreen] define la pantalla splash que aparece al arrancar la aplicación.
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun SplashScreen(
    navController: NavController
) {
    // Animación de escala para la imagen del splash screen
    val scale = remember { Animatable(1f) }

    // Efecto lanzado cuando el componente se crea
    LaunchedEffect(key1 = true) {
        // Animación de escala para reducir el tamaño de la imagen
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )
        delay(2000L)

        // Comprobamos si el usuario está autentificado
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            // Si no está autentificado, navegamos a la pantalla de inicio de sesión
            navController.navigate(LoginDestination.route)
        } else {
            // Si está autentificado, navegamos a la pantalla principal de la aplicación
            navController.navigate(HomeDestination.route) {
                // Evitamos que si damos hacia atrás en el dispositivo vuelva a salir el splash
                popUpTo(SplashDestination.route) {
                    inclusive = true
                }
            }
        }
    }

    // Diseño del splash screen
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = colorResource(id = R.color.my_splash_purple)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 150.dp)
                .scale(scale.value),
        ) {
            // Imagen del logo de la aplicación
            Image(
                painter = painterResource(id = R.drawable.icon_swiftlearn),
                contentDescription = stringResource(R.string.description_logo_icon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

/**
 * Función para previsualizar la pantalla splash de arranque.
 */
@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        navController = rememberNavController()
    )
}