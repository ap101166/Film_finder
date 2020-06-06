package com.otus.android_course.petrov.filmfinder.data

data class FilmItem(
    val caption: String?,
    val description: String?,
    val pictureUrl: String?,
    var isFavorite: Boolean
)

data class FavoriteItem(
    val caption: String?,
    val pictureUrl: String?
)
