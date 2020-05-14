package com.otus.android_course.petrov.filmfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.otus.android_course.petrov.filmfinder.adapters.FavoriteAdapter
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.favoriteFilmItems
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        //
        initFavoritesRecycler()
    }

    private fun initFavoritesRecycler() {
        recyclerViewFavorites.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewFavorites.adapter = FavoriteAdapter(LayoutInflater.from(this), favoriteFilmItems)
    }
}
