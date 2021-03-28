package com.otus.android_course.petrov.filmfinder.repository

import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.data.GlobalObjects.FILM_LIST_CHANGED
import com.otus.android_course.petrov.filmfinder.data.GlobalObjects.favoriteList
import com.otus.android_course.petrov.filmfinder.data.GlobalObjects.filmList
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.web.FilmModel
import com.otus.android_course.petrov.filmfinder.repository.web.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FilmRepository {

    // Номер текущей страницы
    private var curPageNumber = 1

    //
    const val LOAD_ERROR_1 = 1
    const val LOAD_ERROR_2 = 2
    const val EMPTY_RESPONSE = 3

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
            filmList.clear()
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
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
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
                                for (favItem in favoriteList) {
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
                            filmList.addAll(tmpList)
                            //
                            callback.onSuccess(FILM_LIST_CHANGED)
                            curPageNumber++
                        } else {
                            callback.onError(EMPTY_RESPONSE)
                        }
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