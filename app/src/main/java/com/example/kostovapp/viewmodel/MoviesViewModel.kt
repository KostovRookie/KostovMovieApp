package com.example.kostovapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kostovapp.api.TMDbApiService
import com.example.kostovapp.model.Movie
import com.example.kostovapp.model.datastore.DataStoreManager
import com.example.kostovapp.model.room.MovieDao
import com.example.kostovapp.model.room.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val apiService: TMDbApiService,
    private val dataStoreManager: DataStoreManager,
    private val movieDao: MovieDao
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _trailerUrl = MutableStateFlow<String?>(null)
    val trailerUrl: StateFlow<String?> = _trailerUrl.asStateFlow()


    val favorites = dataStoreManager.favoriteMovies.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptySet()
    )

    private val _savedMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val savedMovies: StateFlow<List<MovieEntity>> = _savedMovies

    init {
        loadMovies()
        observeSavedMovies()
    }

    private fun observeSavedMovies() {
        viewModelScope.launch {
            movieDao.getAllMovies().collect { movies ->
                _savedMovies.value =
                    movies
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _isSearching.value = true
            try {
                val response = apiService.searchMovies(
                    apiKey = "0dad83007bea60d99261a92e4eefda99",
                    query = query
                )
                _searchResults.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMovieTrailer(movieId: Int) {
        viewModelScope.launch {
            try {
                val response =
                    apiService.getMovieVideos(movieId, apiKey = "0dad83007bea60d99261a92e4eefda99")
                val trailer = response.results.find { it.site == "YouTube" && it.type == "Trailer" }
                _trailerUrl.value = trailer?.key?.let { "https://www.youtube.com/watch?v=$it" }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val popularResponse =
                    apiService.getPopularMovies(apiKey = "0dad83007bea60d99261a92e4eefda99")
                val trendingResponse =
                    apiService.getTrendingMovies(apiKey = "0dad83007bea60d99261a92e4eefda99")
                val upcomingResponse =
                    apiService.getUpcomingMovies(apiKey = "0dad83007bea60d99261a92e4eefda99")

                _popularMovies.value = popularResponse.results
                _trendingMovies.value = trendingResponse.results
                _upcomingMovies.value = upcomingResponse.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addFavorite(movieId: String) {
        viewModelScope.launch { dataStoreManager.addFavorite(movieId) }
    }

    fun removeFavorite(movieId: String) {
        viewModelScope.launch { dataStoreManager.removeFavorite(movieId) }
    }

    fun saveMovieForLater(movie: Movie) {
        viewModelScope.launch {
            movieDao.insertMovie(
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    posterPath = movie.poster,
                    releaseDate = movie.releaseDate
                )
            )
        }
    }

    fun removeSavedMovie(movie: Movie) {
        viewModelScope.launch {
            val movieEntity = movieDao.getMovieById(movie.id)
            if (movieEntity != null) {
                movieDao.deleteMovie(movieEntity)
            }
        }
    }
}