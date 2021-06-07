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

        //
        lateinit var appInstance: Application
            private set

        // Список любимых фильмов
        val favoriteFilmList = ArrayList<FavoriteFilm>()

        // Список всех фильмов
        val filmList = ArrayList<Film>()
    }
}

//todo
// Разобраться как создаются фрагменты
// Подумать где хранить список фильмов и фаворитов (ViewModel или глобально)
