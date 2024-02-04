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
import com.example.swiftlearn.ui.home.HomeDestination
import com.example.swiftlearn.ui.navigation.SwiftLearnScreens
import com.example.swiftlearn.ui.screens.login.LoginDestination
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    val scale = remember {
        Animatable(1f)
    }
    LaunchedEffect(key1 = true) {
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

        // Comprobamos si está autentificado el usuario
        //   -> si no está autentificado se manda a la pantalla de inicio de sesión
        //   -> si está autentificado se manda a la pantalla principal de la aplicación
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(LoginDestination.route)
        } else {
            navController.navigate(HomeDestination.route) {
                // Evitamos que si damos hacia atrás en el dispositivo vuelva a salir el splash
                popUpTo(SwiftLearnScreens.SplashScreen.name) {
                    inclusive = true
                }
            }
        }
    }
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
            Image(
                painter = painterResource(id = R.drawable.swiftlearn),
                contentDescription = stringResource(R.string.description_logo_icon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        navController = rememberNavController()
    )
}