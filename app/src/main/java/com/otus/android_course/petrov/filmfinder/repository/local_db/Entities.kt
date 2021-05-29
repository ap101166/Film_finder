package com.otus.android_course.petrov.filmfinder.repository.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_films")
data class Film(
    @PrimaryKey
    val id: Int = 0,
    val caption: String?,
    val description: String?,
    val pictureUrl: String?,
    var isFavorite: Boolean = false
)

@Entity(tableName = "fav_films")
data class FavoriteFilm(
    @PrimaryKey
    val id: Int = 0,
    var caption: String?,
    var pictureUrl: String?
)
