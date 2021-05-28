package com.otus.android_course.petrov.filmfinder.repository.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val caption: String?,
    val description: String?,
    val pictureUrl: String?,
    var isFavorite: Boolean = false
)

@Entity
data class FavoriteFilm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var caption: String?,
    var pictureUrl: String?
)
