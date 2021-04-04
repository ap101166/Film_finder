package com.otus.android_course.petrov.filmfinder.repository.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.otus.android_course.petrov.filmfinder.repository.local_db.entities.AllFilms
import com.otus.android_course.petrov.filmfinder.repository.local_db.entities.FavoriteFilms

@Database(entities = [FavoriteFilms::class, AllFilms::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}