package com.example.kostovapp.ui.screens

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kostovapp.R
import com.example.kostovapp.data.model.Movie
import com.example.kostovapp.utils.Constants
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val trailerUrlState = viewModel.trailerUrl.collectAsState()
    val trailerUrl = trailerUrlState.value

    LaunchedEffect(movie.id) {
        viewModel.loadMovieTrailer(movie.id)
    }

    val scale = animateFloatAsState(
        targetValue = if (isFavorite) 1.02f else 1f,
        label = "Favorite Scale"
    )

    val backdropUrl = Constants.TMDB_BACKDROP_BASE_URL + movie.poster
    val placeholderImage = painterResource(R.drawable.profile_picture)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check this movie from KostovIMDB: ${movie.title}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = backdropUrl,
                        error = placeholderImage,
                        placeholder = placeholderImage
                    ),
                    contentDescription = "${movie.title} backdrop",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (!trailerUrl.isNullOrBlank()) {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, trailerUrl.toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp),
                        containerColor = Color.Red
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play Trailer",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .scale(scale.value),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Release: ${movie.releaseDate}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(
                            onClick = {
                                if (isFavorite) viewModel.removeFavorite(movie.id.toString())
                                else viewModel.addFavorite(movie.id.toString())
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                if (isFavorite) stringResource(R.string.remove) else stringResource(
                                    R.string.add
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (isSaved) viewModel.removeSavedMovie(movie)
                                else viewModel.saveMovieForLater(movie)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),

                            ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text(if (isSaved) stringResource(R.string.remove) else stringResource(R.string.watch_later))
                        }
                    }
                }
            }
        }
    }
}