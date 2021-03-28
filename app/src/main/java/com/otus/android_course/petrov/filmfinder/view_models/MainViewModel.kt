package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.GlobalObjects.favoriteList
import com.otus.android_course.petrov.filmfinder.data.GlobalObjects.filmList
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository

class MainViewModel : ViewModel() {

    // Разрешение посылки запроса в сеть (для корректной работы onScroll в FilmListFragment:RecyclerView)
    private var netRequestEnabled = false

    // LiveData на обновление и на ошибку загрузки списка фильмов
    private val filmListChangeMutLiveData = MutableLiveData<Int>()
    private val errorMutLiveData = MutableLiveData<Int>()
    //
    val filmListHasChangedLiveData: LiveData<Int>
        get() = filmListChangeMutLiveData
    //
    val errorLiveData: LiveData<Int>
        get() = errorMutLiveData

    /**
     * \brief Добавление/удаление в список избранного
     */
    fun favoriteSignClick(index: Int) {
        //
        val filmItem = filmList[index]
        filmItem.isFavorite = !filmItem.isFavorite
        //
        if (filmItem.isFavorite) {
            // Добавление в список избранного
            favoriteList.add(FavoriteItem(filmItem.filmId, filmItem.caption, filmItem.pictureUrl))
        } else {
            // Удаление из списка избранного
            for (favItem in favoriteList) {
                if (favItem.filmId == filmItem.filmId) {
                    favoriteList.remove(favItem)
                    break
                }
            }
        }
    }

    /**
     * \brief Получение списка фильмов
     */
    private fun getFilmList(doReload: Boolean) {
        //
        FilmRepository.getFilms(doReload, object : IGetFilmsCallback {
            //
            override fun onSuccess(result: Int) {
                filmListChangeMutLiveData.postValue(result)
                // Очередная страница списка получена, резрешено отправлять запрос не следующую
                netRequestEnabled = true
            }
            //
            override fun onError(error: Int) {
                errorMutLiveData.postValue(error)
            }
        })
        // Был послан запрос текущей страницы списка. Блокировка следующих запросов до получения текущей страницы
        netRequestEnabled = false
    }

    /**
     * \brief Реакция на последнюю позицию в списке фильмов - подгрузка следующей страницы
     */
    fun onLastVisibleItemPosition(): Boolean {
        return if (netRequestEnabled) {
            getFilmList(false)
            true
        } else false
    }

    /**
     * \brief Реакция на swipe - обновление списка фильмов
     */
    fun onSwipeRefresh() {
        getFilmList(true)
    }

    /**
     * \brief Получение списка фильмов при старте приложения
     */
    fun onAppStart() {
        if (filmList.isEmpty()) {
            getFilmList(true)
        }
    }
}