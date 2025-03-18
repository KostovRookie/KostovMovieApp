package com.example.kostovapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kostovapp.bottomnav.BottomNavItem
import com.example.kostovapp.model.Movie
import com.example.kostovapp.ui.screens.FavoritesScreen
import com.example.kostovapp.ui.screens.ForLaterWatchingScreen
import com.example.kostovapp.ui.screens.MovieDetailsScreen
import com.example.kostovapp.ui.screens.MoviesScreen
import com.example.kostovapp.viewmodel.MoviesViewModel

@Composable
fun MoviesNavHost(navController: NavHostController, viewModel: MoviesViewModel) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            MoviesScreen(navController, viewModel)
        }
        composable(BottomNavItem.Favorites.route) {
            FavoritesScreen(navController, viewModel)
        }
        composable(BottomNavItem.Database.route) {
            ForLaterWatchingScreen(navController, viewModel)
        }
        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()

            val moviesState = viewModel.movies.collectAsState()
            val savedMoviesState = viewModel.savedMovies.collectAsState()

            val movie = moviesState.value.find { it.id == movieId }
                ?: savedMoviesState.value.find { it.id == movieId }

            MovieDetailsScreen(movie = movie as Movie?, navController = navController, viewModel = viewModel)
        }
    }
}