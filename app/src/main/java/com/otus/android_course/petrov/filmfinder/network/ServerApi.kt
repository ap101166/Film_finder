package com.otus.android_course.petrov.filmfinder.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {
    @GET("films")
    fun getFilms(): Call<List<FilmModel>>
    //
    @GET("films")
    fun getFilmPage(@Query("pg") pageNum: String): Call<List<FilmModel>>
}