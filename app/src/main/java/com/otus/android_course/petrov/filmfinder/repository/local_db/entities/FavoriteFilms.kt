package com.otus.android_course.petrov.filmfinder.repository.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteFilms(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var caption: String?,
    var pictureUrl: String?
)