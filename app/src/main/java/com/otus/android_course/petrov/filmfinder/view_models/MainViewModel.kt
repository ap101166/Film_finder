package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListener
import com.otus.android_course.petrov.filmfinder.views.FilmListFragment

class MainViewModel : ViewModel() {

    private val filmListMutLiveData = MutableLiveData<List<FilmItem>>()
    private val favoriteListMutLiveData = MutableLiveData<List<FavoriteItem>>()
    private val showFilmDetailMutLiveData = MutableLiveData<Int>()
    private val favoritAddRemoveMutLiveData = MutableLiveData<Int>()

    val filmListLiveData: LiveData<List<FilmItem>>
        get() = filmListMutLiveData

    val favoriteListLiveData: LiveData<List<FavoriteItem>>
        get() = favoriteListMutLiveData

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
}