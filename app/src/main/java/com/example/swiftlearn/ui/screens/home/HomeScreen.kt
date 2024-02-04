package com.example.swiftlearn.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.navigation.HomeNavigation
import com.example.swiftlearn.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.login_title
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val navigationItems = listOf(
        MenuItems.AdvertsItem,
        MenuItems.ClassesItem,
        MenuItems.MapItem,
        MenuItems.ProfileItem
    )

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            NavigationInferior(navController, navigationItems)
        },
        floatingActionButton = {
            FloatingButtonNavigation(navController = navController)
        },
        isFloatingActionButtonDocked = true
    ) {
        HomeNavigation(navController = navController)
    }
}

@Composable
private fun NavigationInferior(
    navController: NavHostController,
    menuItems: List<MenuItems>
) {
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        ),
        backgroundColor = colorResource(id = R.color.my_dark_purple),
        modifier = Modifier
            .height(60.dp)
    ) {
        BottomNavigation(
            backgroundColor = colorResource(id = R.color.my_dark_purple),
            modifier = Modifier
                .height(60.dp)
                .padding(0.dp, 0.dp, 60.dp, 0.dp)
        ) {
            val currentItem by navController.currentBackStackEntryAsState()
            menuItems.forEach { item ->
                val selected = currentItem?.destination?.route == item.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            painter =
                                if(selected) painterResource(id = item.filledIconRes)
                                else painterResource(item.outlinedIconRes),
                            tint = Color.White,
                            contentDescription = stringResource(item.titleRes),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.titleRes),
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Composable
private fun FloatingButtonNavigation(
    navController: NavHostController
) {
    FloatingActionButton(
        onClick = {
            navController.navigate(MenuItems.NewAdvertItem.route)
        },
        backgroundColor = colorResource(id = R.color.my_dark_purple)
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_add),
            contentDescription = stringResource(id = R.string.title_new_advert),
            tint = Color.White,
            modifier = Modifier
                .size(30.dp)

        )
    }
}