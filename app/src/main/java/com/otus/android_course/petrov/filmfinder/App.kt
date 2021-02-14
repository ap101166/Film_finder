package com.otus.android_course.petrov.filmfinder

import android.app.Application
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.network.ServerApi
import kotlinx.android.synthetic.main.film_list_fragment.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initRetrofit()
    }

    /**
     * \brief Инициализация Retrofit
     */
    private fun initRetrofit() {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                })
            .build()

        srvApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(ServerApi::class.java)
    }

    companion object {
        //
        lateinit var srvApi: ServerApi
            private set

        // URL сетевого ресурса
        const val BASE_URL = "https://my-json-server.typicode.com/ap101166/Android-base/"

        // Список любимых фильмов
        val favoriteList = mutableListOf<FavoriteItem>()

        // Список всех фильмов
        val filmList = mutableListOf<FilmItem>()

        // Номер текущей страницы
        var curPageNumber = 1

        // Разрешение посылки запроса в сеть
        var netRequestEnabled = false
    }
}
