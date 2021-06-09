package com.otus.android_course.petrov.filmfinder.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.otus.android_course.petrov.filmfinder.App
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

    // Список фильмов
    private val filmList = ArrayList<Film>()

    // Список любимых фильмов
    private val favoriteFilmList = ArrayList<FavoriteFilm>()

    // LiveData списка фильмов
    private val filmListMutLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = filmListMutLiveData

    // LiveData ошибки загрузки списка фильмов
    private val errorMutLiveData = MutableLiveData<Int>()
    val errorLiveData: LiveData<Int>
        get() = errorMutLiveData

    // Номер текущей страницы
    private var curPageNumber = 1

    // Разрешение посылки запроса в сеть (для корректной работы onScroll в FilmListFragment:RecyclerView)
    private var netRequestEnable = true

    // Ошибка связи с сервером
    const val HARD_LOAD_ERROR = -1

    /**
     * \brief Метод для получения списка фильмов с сервера или из БД
     */
    fun getFilms(doReload: Boolean): Boolean {
        // Блокировка повторных запросов до прихода ответа на предыдущий запрос
        if (!netRequestEnable) return false
        netRequestEnable = false
        // Перезагрузка списка фильмов с первой страницы
        if (doReload) {
            curPageNumber = 1
        }
        // Загрузка текущей страницы
        WebService.getService().getFilmPage(curPageNumber.toString())
            .enqueue(object : Callback<List<FilmModel>> {
                //
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
                            // Инкремент номера страницы
                            curPageNumber++
                        } else {
                            // Пришел пустой ответ (список закончился) - просто обновляем filmList
                        }
                        // Очередная страница успешно загружена или пустой ответ, необх. обновить список фильмов
                        filmListMutLiveData.postValue(filmList)
                    } else {
                        errorMutLiveData.postValue(response.code())
                    }
                    // Ответ получен, можно отправлять следующий запрос
                    netRequestEnable = true
                }
                //
                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    errorMutLiveData.postValue(HARD_LOAD_ERROR)
                    // Неуспешный ответ получен, можно отправлять следующий запрос
                    netRequestEnable = true
                    // Перезапуск сервиса при ошибке соединения
                    WebService.restartService()
                }
            })
        return true
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
                filmListMutLiveData.postValue(filmList)
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
     * \brief Добавление/удаление в список избранного
     */
    fun addOrRemoveFavorites(index: Int): Boolean {
        //
        val film = filmList[index]
        film.isFavorite = !film.isFavorite
        //
        if (film.isFavorite) {
            // Добавление в список избранного
            val tmpFav = FavoriteFilm(film.id, film.caption, film.pictureUrl)
            favoriteFilmList.add(tmpFav)
            Executors.newSingleThreadScheduledExecutor().execute {
                Db.getInstance(App.appInstance)!!.filmDao().insertFavorite(tmpFav)
            }
        } else {
            // Удаление из списка избранного
            for (favItem in favoriteFilmList) {
                if (favItem.id == film.id) {
                    favoriteFilmList.remove(favItem)
                    Executors.newSingleThreadScheduledExecutor().execute {
                        Db.getInstance(App.appInstance)!!.filmDao().deleteFavorite(favItem)
                    }
                    break
                }
            }
        }
        filmListMutLiveData.postValue(filmList)
        return film.isFavorite
    }

    /**
     * \brief Передать список Favorites
     */
    fun getFavoriteList(): List<FavoriteFilm> {
        return favoriteFilmList
    }
}
