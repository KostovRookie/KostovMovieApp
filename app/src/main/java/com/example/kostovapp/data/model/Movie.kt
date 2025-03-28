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

@Parcelize
data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val isoCode: String,
    val name: String
) : Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable