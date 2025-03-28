package com.example.kostovapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kostovapp.R
import com.example.kostovapp.ui.components.MovieItem
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MoviesScreen(
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel(),
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val popularMovies = viewModel.popularMovies.collectAsState()
    val trendingMovies = viewModel.trendingMovies.collectAsState()
    val upcomingMovies = viewModel.upcomingMovies.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val searchText = remember { mutableStateOf(TextFieldValue("")) }
    val searchResults = viewModel.searchResults.collectAsState()
    val isSearching = viewModel.isSearching.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading.value,
        onRefresh = { viewModel.loadMovies() }
    )

    val tabTitles = listOf(
        stringResource(R.string.popular),
        stringResource(R.string.trending),
        stringResource(R.string.upcoming)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .pullRefresh(pullRefreshState)
    ) {

        Column {

            Text(
                text = stringResource(R.string.app_name_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

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
                placeholder = { Text(stringResource(R.string.search_movies)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (!isSearching.value) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent,
                    divider = {}
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (isLoading.value) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val movies =
                        if (isSearching.value) searchResults.value else when (selectedTabIndex) {
                            0 -> popularMovies.value
                            1 -> trendingMovies.value
                            2 -> upcomingMovies.value
                            else -> popularMovies.value
                        }

                    if (movies.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_movies_found))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 16.dp)
                        ) {
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

        PullRefreshIndicator(
            refreshing = isLoading.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}