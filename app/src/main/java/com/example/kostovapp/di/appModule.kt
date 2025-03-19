package com.example.kostovapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.kostovapp.data.datastore.DataStoreManager
import com.example.kostovapp.api.provideTMDbApiService
import com.example.kostovapp.data.room.MovieDatabase
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("favorites")

val appModule = module {
    single { provideTMDbApiService() }
    single { DataStoreManager(get()) }
    single {
        Room.databaseBuilder(androidContext(), MovieDatabase::class.java, "movies_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<MovieDatabase>().movieDao() }

    viewModel { MoviesViewModel(get(), get(), get()) }
}
