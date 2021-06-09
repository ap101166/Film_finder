package com.otus.android_course.petrov.filmfinder

import android.app.Application
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import com.otus.android_course.petrov.filmfinder.repository.local_db.FavoriteFilm
import com.otus.android_course.petrov.filmfinder.repository.local_db.Film

class App : Application() {
    //
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        // Чтение списков фильмов из локальной БД при старте приложения
        FilmRepository.loadFilmsFromDb()
    }

    companion object {
        lateinit var appInstance: Application
            private set
    }
}
