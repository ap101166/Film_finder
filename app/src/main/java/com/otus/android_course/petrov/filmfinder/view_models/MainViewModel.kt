package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListener
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import com.otus.android_course.petrov.filmfinder.views.FilmListFragment

class MainViewModel : ViewModel() {

    private val filmListMutLiveData = MutableLiveData<List<FilmItem>>()
    private val favoriteListMutLiveData = MutableLiveData<List<FavoriteItem>>()
    private val errorMutLiveData = MutableLiveData<String>()

    private val showFilmDetailMutLiveData = MutableLiveData<Int>()
    private val favoritAddRemoveMutLiveData = MutableLiveData<Int>()

    val filmListLiveData: LiveData<List<FilmItem>>
        get() = filmListMutLiveData

    val favoriteListLiveData: LiveData<List<FavoriteItem>>
        get() = favoriteListMutLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutLiveData

    val showFilmDetailLiveData: LiveData<Int>
        get() = showFilmDetailMutLiveData

    val favoritAddRemoveLiveData: LiveData<Int>
        get() = favoritAddRemoveMutLiveData

    // Объект для установки Listeners на .....
    val clickListeners = object : IFilmListClickListener {
        //
        override fun onFilmListClick(index: Int) {
            showFilmDetailMutLiveData.postValue(index)
        }

        //
        override fun onFavoriteClick(index: Int) {
            //
            val filmItem = filmListMutLiveData.value!![index]
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
    fun getFilmList(isReload: Boolean) {
        FilmRepository.loadFilmsFromNet(isReload, object : IGetFilmsCallback {
            //
            override fun onSuccess(filmList: List<FilmItem>) {
                //
                val tmpList = ArrayList<FilmItem>()
                if (!isReload) {
                    tmpList.addAll(filmListMutLiveData.value!!)
                }

                for (film in filmList) {
                    // Проверка, имеется ли загружаемый фильм в списке избранного
                    var isFav = false
                    for (favItem in favoriteListMutLiveData.value!!) {
                        if (favItem.filmId == film.filmId) {
                            isFav = true
                            // Обновление элементов списка избранного т.к. возможно были изменения
                            favItem.caption = film.caption
                            favItem.pictureUrl = film.pictureUrl
                            break
                        }
                    }
                    film.isFavorite = isFav
                }
                tmpList.addAll(filmList)
                //
                filmListMutLiveData.postValue(tmpList)
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