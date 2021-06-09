package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import com.otus.android_course.petrov.filmfinder.repository.local_db.FavoriteFilm

class FilmsViewModel(private val param: Int) : ViewModel() {

    // Признак первого запуска приложения
    private var appStart = true

    // LiveData списка фильмов
    val filmListLiveData by lazy {
        FilmRepository.filmListLiveData
    }

    // LiveData ошибки загрузки списка фильмов
    val errorLiveData by lazy {
        FilmRepository.errorLiveData
    }

    /**
     * \brief Передать список Favorites
     */
    fun getFavoriteList(): List<FavoriteFilm> {
        return FilmRepository.getFavoriteList()
    }

    /**
     * \brief Добавление/удаление в список избранного
     */
    fun favoriteSignClick(index: Int): Boolean {
        return FilmRepository.addOrRemoveFavorites(index)
    }

    /**
     * \brief Реакция на swipe - обновление списка фильмов
     */
    fun onSwipeRefresh() {
        FilmRepository.getFilms(true)
    }

    /**
     * \brief Получение страницы списка фильмов
     */
    fun getFilms(): Boolean {
        return FilmRepository.getFilms(false)
    }

    /**
     * \brief Получение страницы списка фильмов при старте приложения
     */
    fun getFilmsOnStart(): Boolean {
        return if (appStart) {
            FilmRepository.getFilms(true)
            appStart = false
            true
        } else false
    }
}