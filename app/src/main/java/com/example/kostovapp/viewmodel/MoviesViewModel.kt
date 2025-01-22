package com.example.kostovapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kostovapp.BuildConfig
import com.example.kostovapp.TMDbApiService
import com.example.kostovapp.model.DataStoreManager
import com.example.kostovapp.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val apiService: TMDbApiService,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val movies = MutableStateFlow<List<Movie>>(emptyList())
    val isLoading = MutableStateFlow(false)
    val favorites = dataStoreManager.favoriteMovies.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptySet()
    )

    fun loadMovies() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response =
                    apiService.getPopularMovies(BuildConfig.API_KEY)
                movies.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addFavorite(movieId: String) {
        viewModelScope.launch { dataStoreManager.addFavorite(movieId) }
    }

    fun removeFavorite(movieId: String) {
        viewModelScope.launch { dataStoreManager.removeFavorite(movieId) }
    }
}
