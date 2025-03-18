package com.example.kostovapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kostovapp.model.Movie
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    movie: Movie?,
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel()
) {
    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Movie details not available.")
        }
        return
    }

    val favoritesState = viewModel.favorites.collectAsState(emptySet())
    val favorites = favoritesState.value
    val isFavorite = favorites.contains(movie.id.toString())
    val savedMoviesState = viewModel.savedMovies.collectAsState(emptyList())
    val savedMovies = savedMoviesState.value
    val isSaved = savedMovies.any { it.id == movie.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster}"),
            contentDescription = "${movie.title} poster",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Release Date: ${movie.releaseDate}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isFavorite) {
                    viewModel.removeFavorite(movie.id.toString())
                } else {
                    viewModel.addFavorite(movie.id.toString())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (isSaved) {
                    viewModel.removeSavedMovie(movie)
                } else {
                    viewModel.saveMovieForLater(movie)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaved) "Remove from Later Watching" else "Save for Later Watching")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
