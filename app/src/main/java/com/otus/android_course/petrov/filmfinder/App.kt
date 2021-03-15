package com.otus.android_course.petrov.filmfinder

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel
import kotlinx.android.synthetic.main.film_list_fragment.*

class App : Application() {

//    override fun onCreate() {
//        super.onCreate()
//    }

    companion object {

        const val FILM_LIST_CHANGED = 1

        // Список любимых фильмов
        val favoriteList = ArrayList<FavoriteItem>()

        // Список всех фильмов
        val filmList = ArrayList<FilmItem>()
    }
}
