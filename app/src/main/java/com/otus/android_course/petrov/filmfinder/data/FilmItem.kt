package com.otus.android_course.petrov.filmfinder.data

data class FilmItem(
    val filmId: Int,
    val caption: String?,
    val description: String?,
    val pictureUrl: String?,
    var isFavorite: Boolean
)

data class FavoriteItem(
    val filmId: Int,
    var caption: String?,
    var pictureUrl: String?
)
