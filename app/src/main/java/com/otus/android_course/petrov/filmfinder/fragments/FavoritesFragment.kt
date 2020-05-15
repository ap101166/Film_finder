package com.otus.android_course.petrov.filmfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FavoriteAdapter
import com.otus.android_course.petrov.filmfinder.data.favoriteFilmItems

class FavoritesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recyclerViewFavorites).layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.findViewById<RecyclerView>(R.id.recyclerViewFavorites).adapter =
            FavoriteAdapter(LayoutInflater.from(activity), favoriteFilmItems)
    }

    companion object {
        const val TAG = "FavoritesFragment"
    }
}