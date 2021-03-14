package com.otus.android_course.petrov.filmfinder.repository.web_repo

import com.otus.android_course.petrov.filmfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * TODO - дабавить Comment
 *  Инициализация Retrofit
 */

object WebService {

    var service: WebServiceAPI
        private set

    // URL сетевого ресурса       TODO - дабавить в Preferences
    private const val BASE_URL = "https://my-json-server.typicode.com/ap101166/Android-base/"

    // Таймаут на получение ответа в секундах   TODO - дабавить в Preferences
    private const val callTimeOut = 10L

    init {
        //
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            level = HttpLoggingInterceptor.Level.BASIC
                        }
                    })
  //          .connectTimeout(callTimeOut, TimeUnit.SECONDS)    TODO
            .build()
        //
        service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(WebServiceAPI::class.java)
    }
}