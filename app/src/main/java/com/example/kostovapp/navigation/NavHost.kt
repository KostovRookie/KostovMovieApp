package com.example.kostovapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kostovapp.model.Movie
import com.example.kostovapp.ui.screens.FavoritesScreen
import com.example.kostovapp.ui.screens.MovieDetailsScreen
import com.example.kostovapp.ui.screens.MoviesScreen

@Composable
fun MoviesNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "movies") {
        composable("movies") {
            MoviesScreen(navController = navController)
        }
        composable("details") {
            val movie = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Movie>("movie")

            MovieDetailsScreen(movie = movie)
        }
        composable("favorites") {
            FavoritesScreen(navController = navController)
        }
    }
}