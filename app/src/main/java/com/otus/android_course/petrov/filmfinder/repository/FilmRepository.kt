package com.otus.android_course.petrov.filmfinder.repository

import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.App.Companion.favoriteFilmList
import com.otus.android_course.petrov.filmfinder.App.Companion.filmList
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.local_db.Db
import com.otus.android_course.petrov.filmfinder.repository.local_db.FavoriteFilm
import com.otus.android_course.petrov.filmfinder.repository.local_db.Film
import com.otus.android_course.petrov.filmfinder.repository.web.FilmModel
import com.otus.android_course.petrov.filmfinder.repository.web.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

object FilmRepository {

    // Номер текущей страницы
    private var curPageNumber = 1

    //
    const val FILM_LIST_CHANGED = 1
    const val EMPTY_RESPONSE = 2
    const val HARD_LOAD_ERROR = -1

    /**
     * \brief Метод для получения списка фильмов с сервера или из БД
     */
    fun getFilms(doReload: Boolean, callback: IGetFilmsCallback) {
        // Перезагрузка списка фильмов с первой страницы
        if (doReload) {
            curPageNumber = 1
        }
        // Загрузка текущей страницы
        WebService.getService().getFilmPage(curPageNumber.toString())
            .enqueue(object : Callback<List<FilmModel>> {
                // Callback на успешное выполнение запроса
                override fun onResponse(
                    call: Call<List<FilmModel>>,
                    response: Response<List<FilmModel>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
                            val tmpList = ArrayList<Film>()
                            response.body()?.forEach { resp ->
                                tmpList.add(
                                    Film(
                                        id = resp.id,
                                        caption = resp.title,
                                        description = resp.description,
                                        pictureUrl = resp.image
                                    )
                                )
                            }
                            // Установка признака "избранное" в загружаемых фильмах если они есть в списке избранного
                            checkAndSetFavorites(tmpList)
                            // Добавление загруженной страницы в список фильмов и в локальную БД
                            if (curPageNumber == 1) filmList.clear() // Если это первая страница, очистка списка фильмов
                            filmList.addAll(tmpList)
                            Executors.newSingleThreadScheduledExecutor().execute {
                                Db.getInstance(App.appInstance)!!.filmDao().insertFilmList(tmpList as List<Film>)
                            }
                            // Очередная страница успешно загружена
                            callback.onSuccess(FILM_LIST_CHANGED)
                            curPageNumber++
                        } else {
                            // Пришел пустой ответ - нормальная ситуация (размер списка кратен размеру страницы)
                            callback.onSuccess(EMPTY_RESPONSE)
                        }
                    } else {
                        callback.onError(response.code())
                    }
                }

                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    callback.onError(HARD_LOAD_ERROR)
                }
            })
    }

    /**
     * \brief Установка признака "избранное" в загружаемых фильмах если они есть в списке избранного
     */
    private fun checkAndSetFavorites(loadedFilms: List<Film>) {
        //
        for (film in loadedFilms) {
            var isFav = false
            for (favFilm in favoriteFilmList) {
                // Проверка на вхождение в список избранного
                if (favFilm.id == film.id) {
                    isFav = true
                    // Обновление элемента списка избранного если элемент изменился
                    if ((favFilm.caption != film.caption) || (favFilm.pictureUrl != film.pictureUrl)) {
                        favFilm.caption = film.caption
                        favFilm.pictureUrl = film.pictureUrl
                        Executors.newSingleThreadScheduledExecutor().execute {
                            Db.getInstance(App.appInstance)!!.filmDao().updateFavorite(favFilm)
                        }
                    }
                    break
                }
            }
            film.isFavorite = isFav
        }
    }

    /**
     * \brief Чтение списков фильмов из локальной БД
     */
    fun loadFilmsFromDb() {
        //
        Executors.newSingleThreadScheduledExecutor().execute {
            // Чтение списка фильмов из БД
            Db.getInstance(App.appInstance)!!.filmDao().getFilmList().let {
                filmList.clear()
                filmList.addAll(it)
            }
            // Чтение списка избранного из БД
            Db.getInstance(App.appInstance)!!.filmDao().getFavorites().let {
                favoriteFilmList.clear()
                favoriteFilmList.addAll(it)
            }
            //
            checkAndSetFavorites(filmList)
        }
    }

    /**
     * \brief Метод для добавления/удаления в список избранного
     */
    fun addOrRemoveFavorites(filmItem: Film) {
        //
        filmItem.isFavorite = !filmItem.isFavorite
        //
        if (filmItem.isFavorite) {
            // Добавление в список избранного
            val tmpFav = FavoriteFilm(filmItem.id, filmItem.caption, filmItem.pictureUrl)
            favoriteFilmList.add(tmpFav)
            Executors.newSingleThreadScheduledExecutor().execute {
                Db.getInstance(App.appInstance)!!.filmDao().insertFavorite(tmpFav)
            }
        } else {
            // Удаление из списка избранного
            for (favItem in favoriteFilmList) {
                if (favItem.id == filmItem.id) {
                    favoriteFilmList.remove(favItem)
                    Executors.newSingleThreadScheduledExecutor().execute {
                        Db.getInstance(App.appInstance)!!.filmDao().deleteFavorite(favItem)
                    }
                    break
                }
            }
        }
    }
}