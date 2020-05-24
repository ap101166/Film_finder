package com.otus.android_course.petrov.filmfinder.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

    @GET("films")
    fun getFilms(): Call<List<FilmModel>>

    @GET("films?id=1&name=blabla")
    fun getFilmById(@Query("image") id: String, @Query("name") name:String): Call<FilmModel>
}