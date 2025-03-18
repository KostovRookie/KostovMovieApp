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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val apiService: TMDbApiService,
    private val dataStoreManager: DataStoreManager,
    private val movieDao: MovieDao
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    val favorites = dataStoreManager.favoriteMovies.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptySet()
    )

    private val _savedMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val savedMovies: StateFlow<List<MovieEntity>> = _savedMovies

    val isLoading = MutableStateFlow(false)

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

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val response =
                    apiService.getPopularMovies(apiKey = "0dad83007bea60d99261a92e4eefda99")
                _movies.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
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