package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.App.Companion.FILM_LIST_CHANGED
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListeners
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository

class MainViewModel : ViewModel() {

    private val filmListChangeMutLiveData = MutableLiveData<Int>()
    private val errorMutLiveData = MutableLiveData<String>()

    private val showFilmDetailMutLiveData = MutableLiveData<Int>()
    private val favoritAddRemoveMutLiveData = MutableLiveData<Int>()

    val filmListChangeLiveData: LiveData<Int>
        get() = filmListChangeMutLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutLiveData

    val showFilmDetailLiveData: LiveData<Int>
        get() = showFilmDetailMutLiveData

    val favoritAddRemoveLiveData: LiveData<Int>
        get() = favoritAddRemoveMutLiveData

    // Методы для обработчиков нажатий на элемент списка фильмов
    val filmClickListeners = object : IFilmListClickListeners {
        //
        override fun onFilmItemClick(index: Int) {
            showFilmDetailMutLiveData.postValue(index)
        }

        //
        override fun onFavoriteSignClick(index: Int) {
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
            //
            favoritAddRemoveMutLiveData.postValue(index)
        }
    }

    //
    fun getFilmList(doReload: Boolean) {
        //
        FilmRepository.getFilms(doReload, object : IGetFilmsCallback {
            //
            override fun onSuccess(result: Int) {
                filmListChangeMutLiveData.postValue(result)
            }
            //
            override fun onError(error: String) {
                errorMutLiveData.postValue(error)
            }
        })
    }

    // Реакция на swipe - обновление списка фильмов
    fun onSwipeRefresh() {
        App.netRequestEnabled = false
        getFilmList(true)
    }

}