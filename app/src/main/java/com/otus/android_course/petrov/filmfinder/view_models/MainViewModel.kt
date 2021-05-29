package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import com.otus.android_course.petrov.filmfinder.repository.local_db.Film
import kotlinx.android.synthetic.main.film_list_fragment.*

class MainViewModel(private val param: Int) : ViewModel() {

    // Признак первого запуска приложения
    var appStart = true

    // Разрешение посылки запроса в сеть (для корректной работы onScroll в FilmListFragment:RecyclerView)
    private var netRequestEnabled = true

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
    fun favoriteSignClick(filmItem: Film) {
        //
        FilmRepository.addOrRemoveFavorites(filmItem)
        filmListChangeMutLiveData.postValue(FilmRepository.FILM_LIST_CHANGED)
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
     * \brief Реакция на swipe - обновление списка фильмов
     */
    fun onSwipeRefresh() {
        getFilmList(true)
    }

    /**
     * \brief Получение страницы списка фильмов
     */
    fun getFilms(): Boolean {
        return if (netRequestEnabled) {
            getFilmList(false)
            true
        } else false
    }

    /**
     * \brief Получение страницы списка фильмов при старте приложения
     */
    fun getFilmsOnStart(): Boolean {
        return if (appStart) {
            getFilmList(true)
            appStart = false
            true
        } else false
    }


}