package com.otus.android_course.petrov.filmfinder.interfaces

interface IGetFilmsCallback {
    fun onSuccess(result: Int)
    fun onError(error: String)
}