package com.otus.android_course.petrov.filmfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.MainActivity
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.viewholders.FavoriteViewHolder
import com.otus.android_course.petrov.filmfinder.viewholders.FilmViewHolder

class FilmAdapter(
    private val inflater: LayoutInflater,
    private val items: List<FilmItem>,
    val listener: (FilmItem, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is FilmViewHolder) {
            holder.bind(item, position)
            holder.itemView.setOnClickListener { listener(item, position) }
        }
    }
}