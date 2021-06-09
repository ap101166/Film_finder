package com.otus.android_course.petrov.filmfinder.repository.web

import com.otus.android_course.petrov.filmfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WebService {

    // URL сетевого ресурса
    private const val BASE_URL = "https://my-json-server.typicode.com/ap101166/Android-base/"

    private const val callTimeOutSec = 30L

    private var service: WebServiceAPI = createService()

    /**
     * \brief Создание сервиса
     */
    private fun createService(): WebServiceAPI {

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            level = HttpLoggingInterceptor.Level.BASIC
                        }
                    })
            .connectTimeout(callTimeOutSec, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(WebServiceAPI::class.java)
    }

    /**
     * \brief Получение сервиса
     */
    fun getService(): WebServiceAPI {
        return service
    }

    /**
     * \brief Перезапуск сервиса
     */
    fun restartService() {
        service = createService()
    }
}