package com.example.kostovapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.kostovapp.R
import com.example.kostovapp.data.model.Movie
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForLaterWatchingScreen(
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val savedMoviesState = viewModel.savedMovies.collectAsState(emptyList())
    val savedMovies = savedMoviesState.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (savedMovies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    stringResource(R.string.no_movies_saved)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(savedMovies) { movieEntity ->
                    FavoriteMovieItem(
                        movie = Movie(
                            id = movieEntity.id,
                            title = movieEntity.title,
                            overview = movieEntity.overview,
                            poster = movieEntity.posterPath,
                            releaseDate = movieEntity.releaseDate
                        ),
                        onClick = {
                            navController.navigate("details/${movieEntity.id}")
                        },
                        onRemoveClick = {}
                    )
                }
            }
        }
    }
}