package com.otus.android_course.petrov.filmfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FavoriteAdapter
import com.otus.android_course.petrov.filmfinder.data.favoriteItems

class FavoritesFragment : Fragment() {

    /**
    \brief Событие создания фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
    \brief Создание визуального интерфейса фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    /**
    \brief Создание списка RecyclerView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewFavor = view.findViewById<RecyclerView>(R.id.recyclerViewFavorites)
        recyclerViewFavor.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewFavor.adapter =
            FavoriteAdapter(LayoutInflater.from(activity), favoriteItems)
        recyclerViewFavor.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(resources.getDrawable(R.drawable.divider, null))
            })
    }

    companion object {
        const val TAG = "FavoritesFragment"
    }
}