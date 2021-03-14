package com.otus.android_course.petrov.filmfinder.repository

import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.App.Companion.FILM_LIST_CHANGED
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.web_repo.FilmModel
import com.otus.android_course.petrov.filmfinder.repository.web_repo.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FilmRepository {

    // Номер текущей страницы
    private var curPageNumber = 1
    //
    const val LOAD_ERROR_1 = 1
    const val LOAD_ERROR_2 = 2

    /**
     * \brief Метод для получения списка фильмов с сервера
     */
    fun getFilms(doReload: Boolean, callback: IGetFilmsCallback) {

        //
        getFilmsFromWeb(doReload, callback)

    }

    /**
     * \brief Метод для получения списка фильмов с сервера
     */
    private fun getFilmsFromWeb(doReload: Boolean, callback: IGetFilmsCallback) {
        // Перезагрузка списка фильмов
        if (doReload) {
            App.filmList.clear()
            curPageNumber = 1
        }
        // Загрузка текущей страницы
        WebService.service.getFilmPage(curPageNumber.toString())
            .enqueue(object : Callback<List<FilmModel>> {
                // Callback на успешное выполнение запроса
                override fun onResponse(
                    call: Call<List<FilmModel>>,
                    response: Response<List<FilmModel>>
                ) {
                    val tmpList = ArrayList<FilmItem>()
                    if (response.isSuccessful && (response.body()!!.isNotEmpty())) {
                        response.body()?.forEach { resp ->
                            tmpList.add(
                                FilmItem(
                                    filmId = resp.id,
                                    caption = resp.title,
                                    description = resp.description,
                                    pictureUrl = resp.image
                                )
                            )
                        }
                        // Проверка, имеется ли загружаемый фильм в списке избранного
                        for (film in tmpList) {
                            var isFav = false
                            for (favItem in App.favoriteList) {
                                // Обновление элементов списка избранного т.к. возможно были изменения
                                if (favItem.filmId == film.filmId) {
                                    isFav = true
                                    favItem.caption = film.caption
                                    favItem.pictureUrl = film.pictureUrl
                                    break
                                }
                            }
                            film.isFavorite = isFav
                        }
                        //
                        App.filmList.addAll(tmpList)
                        //
                        callback.onSuccess(FILM_LIST_CHANGED)
                        curPageNumber++
                    } else {
                        callback.onError(LOAD_ERROR_1)
                    }
                }
                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    callback.onError(LOAD_ERROR_2)
                }
            })
    }
}