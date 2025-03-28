package com.example.kostovapp.navigationcomponents

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        BottomNavItem.items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                    when (item) {
                        BottomNavItem.Home -> {
                            if (navController.currentDestination?.route == BottomNavItem.Home.route) {
                                navController.navigate(BottomNavItem.Home.route) {
                                    popUpTo(BottomNavItem.Home.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(BottomNavItem.Home.route)
                            }
                        }

                        BottomNavItem.Favorites -> navController.navigate(BottomNavItem.Favorites.route)
                        BottomNavItem.Database -> navController.navigate(BottomNavItem.Database.route)
                    }
                }
            )
        }
    }
}