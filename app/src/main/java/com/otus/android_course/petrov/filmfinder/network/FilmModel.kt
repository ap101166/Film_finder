package com.otus.android_course.petrov.filmfinder.network

import com.google.gson.annotations.SerializedName

data class FilmModel(
    @SerializedName("image_url") val image: String,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("descript") val description: String
)