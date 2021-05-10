package com.otus.android_course.petrov.filmfinder

import android.app.Application
import android.util.Log
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.repository.local_db.Db
import com.otus.android_course.petrov.filmfinder.repository.local_db.FavoriteFilms
import java.util.concurrent.Executors

class App : Application() {
    //
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        // Чтение списка избранного из БД при старте приложения
        Executors.newSingleThreadScheduledExecutor().execute {
            Db.getInstance(this)?.getFilmDao()?.getAll()?.let { favoriteList.addAll(it) }
            Log.d("asd123", "getAll: ${favoriteList.size}")
        }
    }

    companion object {

        //
        lateinit var appInstance: Application
            private set

        // Список любимых фильмов
        val favoriteList = ArrayList<FavoriteFilms>()

        // Список всех фильмов
        val filmList = ArrayList<FilmItem>()
    }
}

//todo
// Разобраться как создаются фрагменты
// Подумать где хранить список фильмов и фаворитов (ViewModel или глобально)
// Как загружать список фильмов при старте