package com.otus.android_course.petrov.filmfinder.repository

import android.widget.Toast
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IGetFilmsCallback
import com.otus.android_course.petrov.filmfinder.repository.web.FilmModel
import com.otus.android_course.petrov.filmfinder.repository.web.WebService
import kotlinx.android.synthetic.main.film_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FilmRepository {
    /**
     * \brief Метод для получения списка фильмов с сервера
     */
    fun loadFilmsFromNet(isReload: Boolean, callback: IGetFilmsCallback) {
        // Перезагрузка списка фильмов с начала
        if (isReload) App.curPageNumber = 1
        //
        WebService.service.getFilmPage(App.curPageNumber.toString()) // Загрузка текущей страницы
            .enqueue(object : Callback<List<FilmModel>> {
                // Callback на успешное выполнение запроса
                override fun onResponse(
                    call: Call<List<FilmModel>>,
                    response: Response<List<FilmModel>>
                ) {
                    val tmpList = ArrayList<FilmItem>()
                    val respSize: Int = response.body()!!.size
                    if (response.isSuccessful && (respSize > 0)) {
                        response.body()
                            ?.forEach { resp ->
                                tmpList.add(
                                    FilmItem(
                                        filmId = resp.id,
                                        caption = resp.title,
                                        description = resp.description,
                                        pictureUrl = resp.image
                                    )
                                )
                            }
                        App.netRequestEnabled = true
                        App.curPageNumber++
                    } else{
                       // TODO ---
                    }
                }
                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    callback.onError("Network error: $t")
                }
            })
    }

}