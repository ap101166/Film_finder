package com.otus.android_course.petrov.filmfinder.interfaces

// Интерфейс обработчиков нажатий на элемент списка фильмов
interface IFilmListClickListeners {
    // Метод для вывода описания фильма
    fun onFilmItemClick(index: Int)
    // Метод для удаления/добавления в список избранного
    fun onFavoriteSignClick(index: Int)
}

