package com.otus.android_course.petrov.filmfinder.interfaces

import com.otus.android_course.petrov.filmfinder.data.FilmItem

interface IGetFilmsCallback {
    fun onSuccess(filmList: List<FilmItem>)
    fun onError(error: String)
}