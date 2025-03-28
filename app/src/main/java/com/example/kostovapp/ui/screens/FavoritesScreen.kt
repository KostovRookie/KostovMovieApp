package com.example.kostovapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.kostovapp.R
import com.example.kostovapp.data.model.Movie
import com.example.kostovapp.utils.Constants
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val movies by viewModel.favorites.collectAsState()
    val favoritesState = viewModel.favorites.collectAsState()
    val favorites = favoritesState.value

    val popularMoviesState = viewModel.popularMovies.collectAsState()
    val trendingMoviesState = viewModel.trendingMovies.collectAsState()
    val upcomingMoviesState = viewModel.upcomingMovies.collectAsState()

    val popularMovies = popularMoviesState.value
    val trendingMovies = trendingMoviesState.value
    val upcomingMovies = upcomingMoviesState.value

    val favoriteMovies = listOf(
        popularMovies,
        trendingMovies,
        upcomingMovies
    ).flatten()
        .filter { favorites.contains(it.id.toString()) }
        .distinctBy { it.id }

    LaunchedEffect(Unit) {
        if (movies.isEmpty()) {
            viewModel.loadMovies()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (favoriteMovies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_favorites))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favoriteMovies) { movie ->
                    FavoriteMovieItem(
                        movie = movie,
                        onClick = {
                            navController.navigate("details/${movie.id}")
                        },
                        onRemoveClick = { viewModel.removeFavorite(movie.id.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteMovieItem(
    movie: Movie,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        val posterUrl = movie.poster?.let {
            Constants.TMDB_IMAGE_BASE_URL + it
        }
            ?: Constants.TMDB_NO_IMAGE_PLACEHOLDER
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(posterUrl)
                    .crossfade(true)
                    .build()
            ),
            contentDescription = "${movie.title} poster",
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(2f / 3f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Release Date: ${movie.releaseDate}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Remove from favorites"
            )
        }
    }
}