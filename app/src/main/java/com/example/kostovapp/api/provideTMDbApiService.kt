package com.example.kostovapp.api

import com.example.kostovapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideTMDbApiService(): TMDbApiService {
    return Retrofit.Builder()
        .baseUrl(Constants.TMDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDbApiService::class.java)
}