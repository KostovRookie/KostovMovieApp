package com.example.kostovapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kostovapp.ui.composables.MovieItem
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel(),
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val popularMovies by viewModel.popularMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchText = remember { mutableStateOf(TextFieldValue("")) }
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val tabTitles = listOf("Popular", "Trending", "Upcoming")

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { newText ->
                searchText.value = newText
                if (newText.text.isNotBlank()) {
                    viewModel.searchMovies(newText.text)
                } else {
                    viewModel.loadMovies()
                }
            },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        if (!isSearching) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 16.dp
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        text = { Text(title) }
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val movies = if (isSearching) searchResults else when (selectedTabIndex) {
                    0 -> popularMovies
                    1 -> trendingMovies
                    2 -> upcomingMovies
                    else -> popularMovies
                }

                if (movies.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No movies found.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(movies) { movie ->
                            MovieItem(
                                movie = movie,
                                isFavorite = viewModel.favorites.collectAsState(emptySet()).value.contains(
                                    movie.id.toString()
                                ),
                                onFavoriteClick = {
                                    if (viewModel.favorites.value.contains(movie.id.toString())) {
                                        viewModel.removeFavorite(movie.id.toString())
                                    } else {
                                        viewModel.addFavorite(movie.id.toString())
                                    }
                                },
                                onClick = {
                                    navController.navigate("details/${movie.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}