package com.example.kostovapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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