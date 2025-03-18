package com.example.kostovapp.ui.screens

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kostovapp.R
import com.example.kostovapp.model.Movie
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    movie: Movie?,
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val context = LocalContext.current

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Movie details not available.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    val favoritesState = viewModel.favorites.collectAsState(emptySet())
    val favorites = favoritesState.value
    val isFavorite = favorites.contains(movie.id.toString())
    val savedMoviesState = viewModel.savedMovies.collectAsState(emptyList())
    val savedMovies = savedMoviesState.value
    val isSaved = savedMovies.any { it.id == movie.id }

    val trailerUrl = viewModel.trailerUrl.collectAsState()
    LaunchedEffect(movie.id) {
        viewModel.loadMovieTrailer(movie.id)
    }

    var isHovered = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (isHovered.value) 1.02f else 1f,
        label = "Hover Effect"
    )

    val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster}"
    val placeholderImage = painterResource(R.drawable.profile_picture)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(12.dp))
                .scale(scale.value)
                .clickable { isHovered.value = !isHovered.value },
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = posterUrl,
                    error = placeholderImage,
                    placeholder = placeholderImage
                ),
                contentDescription = "${movie.title} poster",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Release Date: ${movie.releaseDate}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFavorite) {
                    viewModel.removeFavorite(movie.id.toString())
                } else {
                    viewModel.addFavorite(movie.id.toString())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary)
        ) {
            Text(
                if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                color = Color.White
            )
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

        Spacer(modifier = Modifier.height(16.dp))

        if (!trailerUrl.value.isNullOrBlank()) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, trailerUrl.value!!.toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Watch Trailer 🎬", color = Color.White)
            }
        } else {
            Text(
                text = "No trailer available.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}