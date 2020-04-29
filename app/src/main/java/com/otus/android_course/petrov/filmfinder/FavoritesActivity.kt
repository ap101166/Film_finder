package com.otus.android_course.petrov.filmfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        //
        initFavoritesRecycler()
    }

    private fun initFavoritesRecycler() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerViewFavorites)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = layoutManager
        recycler.adapter = FilmAdapter(MainActivity.FAVORITE_LIST, LayoutInflater.from(this), favoriteFilmItems) { _, _ -> }
    }
}
