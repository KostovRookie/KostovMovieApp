package com.example.kostovapp.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TMDbResponse(
    val results: List<Movie>
)

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("release_date")
    val releaseDate: String
) : Parcelable