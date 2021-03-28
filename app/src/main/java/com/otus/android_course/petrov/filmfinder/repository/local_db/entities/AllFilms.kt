package com.otus.android_course.petrov.filmfinder.repository.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AllFilms(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val caption: String?,
    val description: String?,
    val pictureUrl: String?,
    var isFavorite: Boolean = false
)