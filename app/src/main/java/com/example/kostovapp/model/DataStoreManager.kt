package com.example.kostovapp.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.kostovapp.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager(private val context: Context) {

    private val favoriteMovieKey = stringSetPreferencesKey("favorite_movies")

    val favoriteMovies: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[favoriteMovieKey] ?: emptySet()
        }

    suspend fun addFavorite(movieId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoriteMovieKey] ?: emptySet()
            preferences[favoriteMovieKey] = currentFavorites + movieId
        }
    }

    suspend fun removeFavorite(movieId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoriteMovieKey] ?: emptySet()
            preferences[favoriteMovieKey] = currentFavorites - movieId
        }
    }
}