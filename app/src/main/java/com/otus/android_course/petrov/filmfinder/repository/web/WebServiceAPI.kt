package com.otus.android_course.petrov.filmfinder.repository.web

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceAPI {
    @GET("films")
    fun getAllFilms(): Call<List<FilmModel>>
    //
    @GET("films")
    fun getFilmPage(@Query("pg") pageNum: String): Call<List<FilmModel>>
}