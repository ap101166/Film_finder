package com.otus.android_course.petrov.filmfinder.interfaces

import com.otus.android_course.petrov.filmfinder.data.FilmItem

interface OnRecyclersClickListener {
    fun onFilmListClick(item: FilmItem)
    fun onFavoriteClick(index: Int)
}
