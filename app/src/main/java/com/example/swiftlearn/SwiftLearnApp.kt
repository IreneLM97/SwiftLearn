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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.swiftlearn.ui.navigation.SwiftLearnNavigation

/**
 * Top level composable that represents screens for the application.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SwiftLearnApp(
    navController: NavHostController = rememberNavController()
) {
    SwiftLearnNavigation(navController = navController)
}

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


