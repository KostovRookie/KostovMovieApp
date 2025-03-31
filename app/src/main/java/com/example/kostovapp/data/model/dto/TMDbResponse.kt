package com.example.kostovapp.data.model.dto

import com.example.kostovapp.data.model.Movie


data class TMDbResponse(
    val results: List<Movie>
)