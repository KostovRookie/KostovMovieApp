package com.example.kostovapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kostovapp.navigationcomponents.BottomNavItem
import com.example.kostovapp.ui.screens.FavoritesScreen
import com.example.kostovapp.ui.screens.ForLaterWatchingScreen
import com.example.kostovapp.ui.screens.MovieDetailsScreen
import com.example.kostovapp.ui.screens.MoviesScreen
import com.example.kostovapp.viewmodel.MoviesViewModel

@Composable
fun MoviesNavHost(
    navController: NavHostController,
    viewModel: MoviesViewModel,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            MoviesScreen(navController, viewModel, selectedTabIndex, onTabSelected)
        }
        composable(BottomNavItem.Favorites.route) {
            FavoritesScreen(navController, viewModel)
        }
        composable(BottomNavItem.Database.route) {
            ForLaterWatchingScreen(navController, viewModel)
        }
        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()

            val movie = viewModel.popularMovies.value.find { it.id == movieId }
                ?: viewModel.trendingMovies.value.find { it.id == movieId }
                ?: viewModel.upcomingMovies.value.find { it.id == movieId }
                ?: viewModel.searchResults.value.find { it.id == movieId }

            MovieDetailsScreen(movie = movie, navController = navController, viewModel = viewModel)
        }
    }
}