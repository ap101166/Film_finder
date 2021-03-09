package com.otus.android_course.petrov.filmfinder.interfaces

// Интерфейс обработчиков нажатий на элемент списка фильмов
interface IFilmListClickListener {
    // Метод для вывода описания фильма
    fun onFilmListClick(index: Int)
    // Метод для удаления/добавления в список избранного
    fun onFavoriteClick(index: Int)
}

