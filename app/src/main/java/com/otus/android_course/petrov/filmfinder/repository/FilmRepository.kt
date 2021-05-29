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
    const val FILM_LIST_CHANGED = 0
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
        WebService.getService().getFilmPage(curPageNumber.toString())
            .enqueue(object : Callback<List<FilmModel>> {
                // Callback на успешное выполнение запроса
                override fun onResponse(
                    call: Call<List<FilmModel>>,
                    response: Response<List<FilmModel>>
                ) {
                    val tmpList = ArrayList<Film>()
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
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
                            // Проверка, имеется ли загружаемый фильм в списке избранного
                            for (film in tmpList) {
                                var isFav = false
                                for (favItem in favoriteFilmList) {
                                    // Проверка на вхождение в список избранного
                                    if (favItem.id == film.id) {
                                        isFav = true
                                        // Обновление элемента списка избранного если он изменился
                                        if ((favItem.caption != film.caption) || (favItem.pictureUrl != film.pictureUrl)) {
                                            favItem.caption = film.caption
                                            favItem.pictureUrl = film.pictureUrl
                                            Executors.newSingleThreadScheduledExecutor().execute {
                                                Db.getInstance(App.appInstance)!!.filmDao().updateFavorite(favItem)
                                            }
                                        }
                                        break
                                    }
                                }
                                film.isFavorite = isFav
                            }
                            // Добавление загруженной страницы в список фильмов и в локальную БД
                            filmList.addAll(tmpList)
                            Executors.newSingleThreadScheduledExecutor().execute {
                                Db.getInstance(App.appInstance)!!.filmDao().insertFilmList(tmpList as List<Film>)
                            }
                            //
                            callback.onSuccess(FILM_LIST_CHANGED)
                            curPageNumber++
                        } else {
                            callback.onSuccess(EMPTY_RESPONSE)
                        }
                    } else {
                        callback.onError(response.code())
                    }
                }

                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    callback.onError(LOAD_ERROR_2)
                }
            })
    }

    /**
     * \brief Чтение списка избранного из БД
     */
    fun readFavorites() {
        //
        Executors.newSingleThreadScheduledExecutor().execute {
            Db.getInstance(App.appInstance)!!.filmDao().getFavorites().let {
                favoriteFilmList.clear()
                favoriteFilmList.addAll(it)
            }

//            Db.getInstance(App.appInstance)!!.filmDao().getFilmList().let {
//                filmList.clear()
//                filmList.addAll(it)
//            }

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