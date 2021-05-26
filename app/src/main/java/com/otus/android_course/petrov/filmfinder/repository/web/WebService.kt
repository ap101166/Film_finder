package com.otus.android_course.petrov.filmfinder.repository.web

import com.otus.android_course.petrov.filmfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * TODO - дабавить Comment
 *  Инициализация Retrofit
 */

object WebService {

    // URL сетевого ресурса       TODO - дабавить в Preferences
    private const val BASE_URL = "https://my-json-server.typicode.com/ap101166/Android-base/"

    // Таймаут на получение ответа в секундах   TODO - дабавить в Preferences
    private const val callTimeOutSec = 10L

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                })
        //          .connectTimeout(callTimeOut, TimeUnit.SECONDS)    TODO
        .build()

    private val service: WebServiceAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(WebServiceAPI::class.java)

    fun getService(): WebServiceAPI {
        return service
    }
}