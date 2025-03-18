package com.example.kostovapp.bottomnav

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
        val items = listOf("Home", "Favorites", "Watch Later")

        items.forEachIndexed { index, title ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (title) {
                            "Home" -> Icons.Default.Home
                            "Favorites" -> Icons.Default.Favorite
                            "Watch Later" -> Icons.Default.Check
                            else -> Icons.Default.Home
                        },
                        contentDescription = title
                    )
                },
                label = { Text(title) },
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                    when (title) {
                        "Home" -> {
                            if (navController.currentDestination?.route == "movies") {
                                navController.navigate("movies") {
                                    popUpTo("movies") { inclusive = true }
                                }
                            } else {
                                navController.navigate("movies")
                            }
                        }

                        "Favorites" -> navController.navigate("favorites")
                        "Watch Later" -> navController.navigate("forLaterWatching")
                    }
                }
            )
        }
    }
}