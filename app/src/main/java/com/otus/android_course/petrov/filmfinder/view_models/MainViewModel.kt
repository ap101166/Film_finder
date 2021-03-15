package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.App.Companion.FILM_LIST_CHANGED
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListeners
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import kotlinx.android.synthetic.main.film_list_fragment.*

class MainViewModel : ViewModel() {

    // Разрешение посылки запроса в сеть (для корректной работы onScroll в FilmListFragment:RecyclerView)
    private var netRequestEnabled = false

    // LiveData на обновление и на ошибку загрузки списка фильмов
    private val filmListChangeMutLiveData = MutableLiveData<Int>()
    private val errorMutLiveData = MutableLiveData<Int>()

    val filmListChangeLiveData: LiveData<Int>
        get() = filmListChangeMutLiveData

    val errorLiveData: LiveData<Int>
        get() = errorMutLiveData

    // Метод для добавления/удаления в список избранного
    fun favoriteSignClick(index: Int) {
        //
        val filmItem = App.filmList[index]
        filmItem.isFavorite = !filmItem.isFavorite
        //
        if (filmItem.isFavorite) {
            // Добавление в список избранного
            App.favoriteList.add(FavoriteItem(filmItem.filmId, filmItem.caption, filmItem.pictureUrl))
        } else {
            // Удаление из списка избранного
            for (favItem in App.favoriteList) {
                if (favItem.filmId == filmItem.filmId) {
                    App.favoriteList.remove(favItem)
                    break
                }
            }
        }
    }

    //
    private fun getFilmList(doReload: Boolean) {
        //
        FilmRepository.getFilms(doReload, object : IGetFilmsCallback {
            //
            override fun onSuccess(result: Int) {
                filmListChangeMutLiveData.postValue(result)
                netRequestEnabled = true
            }

            //
            override fun onError(error: Int) {
                errorMutLiveData.postValue(error)
            }
        })
    }

    //
    private var fStart = true
    fun getFilmListOnStart(): Boolean {
        val res = fStart
        if (fStart) {
            onSwipeRefresh()
            fStart = false
        }
        return res
    }

    // Реакция на Scroll в списке фильмов
    fun onScroll(): Boolean {
        val res = netRequestEnabled
        if (netRequestEnabled) {
            getFilmList(false)
            netRequestEnabled = false
        }
        return res
    }

    // Реакция на swipe - обновление списка фильмов
    fun onSwipeRefresh() {
        getFilmList(true)
        netRequestEnabled = false
    }
}