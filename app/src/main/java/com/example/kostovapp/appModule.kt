package com.example.kostovapp

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.kostovapp.model.DataStoreManager
import com.example.kostovapp.model.provideTMDbApiService
import com.example.kostovapp.viewmodel.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("favorites")

val appModule = module {
        single { provideTMDbApiService() }


    single { DataStoreManager(get()) }

    viewModel { MoviesViewModel(get(), get()) }
}
