package com.example.swiftlearn.ui.components

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.swiftlearn.R
import com.example.swiftlearn.ui.navigation.MenuItems
import com.example.swiftlearn.ui.navigation.professorMenuItems
import com.example.swiftlearn.ui.navigation.studentMenuItems

@Composable
fun NavInfProfessor(
    navController: NavHostController,
    menuItems: List<MenuItems>
) {
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        ),
        elevation = 8.dp,
        backgroundColor = colorResource(id = R.color.my_white),
        modifier = Modifier
            .height(60.dp)
    ) {
        BottomNavigation(
            backgroundColor = colorResource(id = R.color.my_white),
            elevation = 0.dp,
            modifier = Modifier
                .height(60.dp)
                .padding(0.dp, 0.dp, 60.dp, 0.dp)
        ) {
            val currentItem by navController.currentBackStackEntryAsState()
            menuItems.forEach { item ->
                val selected = currentItem?.destination?.route == item.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        Icon(
                            painter =
                                if(selected) painterResource(id = item.filledIconRes)
                                else painterResource(item.outlinedIconRes),
                            tint = colorResource(id = R.color.my_dark_purple),
                            contentDescription = stringResource(item.titleRes),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.titleRes),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.my_dark_purple)
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Composable
fun NavInfStudent(
    navController: NavHostController,
    menuItems: List<MenuItems>
) {
    BottomAppBar(
        backgroundColor = colorResource(id = R.color.my_white),
        elevation = 8.dp,
        modifier = Modifier
            .height(60.dp)
    ) {
        BottomNavigation(
            backgroundColor = colorResource(id = R.color.my_white),
            elevation = 0.dp,
            modifier = Modifier
                .height(60.dp)
        ) {
            val currentItem by navController.currentBackStackEntryAsState()
            menuItems.forEach { item ->
                val selected = currentItem?.destination?.route == item.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        Icon(
                            painter =
                            if(selected) painterResource(id = item.filledIconRes)
                            else painterResource(item.outlinedIconRes),
                            tint = colorResource(id = R.color.my_dark_purple),
                            contentDescription = stringResource(item.titleRes),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.titleRes),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.my_dark_purple)
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Composable
fun FloatingButtonNavigation(
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

@Preview
@Composable
fun NavInfProfessorPreview() {
    NavInfProfessor(rememberNavController(), professorMenuItems)
}

@Preview
@Composable
fun NavInfStudentPreview() {
    NavInfStudent(rememberNavController(), studentMenuItems)
}