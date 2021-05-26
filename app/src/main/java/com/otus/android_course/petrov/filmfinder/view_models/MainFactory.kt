package com.otus.android_course.petrov.filmfinder.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainFactory(private val param: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(param) as T
    }
}