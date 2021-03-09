package com.otus.android_course.petrov.filmfinder

import android.app.Application
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {

        // Список любимых фильмов
        val favoriteList = mutableListOf<FavoriteItem>()

        // Список всех фильмов
        val filmList = mutableListOf<FilmItem>()

        // Номер текущей страницы
        var curPageNumber = 1

        // Разрешение посылки запроса в сеть
        var netRequestEnabled = false
    }
}
