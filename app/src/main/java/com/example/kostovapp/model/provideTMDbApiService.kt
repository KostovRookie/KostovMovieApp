package com.example.kostovapp.model

import com.example.kostovapp.TMDbApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideTMDbApiService(): TMDbApiService {
    return Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDbApiService::class.java)
}
