package com.otus.android_course.petrov.filmfinder

import android.app.Application
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.network.ServerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var srvApi: ServerApi

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit()
    }

    /**
    \brief Инициализация Retrofit
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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        srvApi = retrofit.create(ServerApi::class.java)
    }

    companion object {
        //
        lateinit var instance: App
            private set

        // URL сетевого ресурса
        const val BASE_URL = "https://my-json-server.typicode.com/ap101166/Android-base/"

        // Список любимых фильмов
        var favoriteItems = mutableListOf<FavoriteItem>()

        // Список всех фильмов
        val allFilmItems = mutableListOf<FilmItem>()

        // Номер текущей страницы
        var curPageNumber = 1

        // Разрешение посылки запроса в сеть
        var enableNetRequest = false
    }
}
