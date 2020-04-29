package com.otus.android_course.petrov.filmfinder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilmAdapter(
    private val listType: Int,
    private val inflater: LayoutInflater,
    private val items: List<FilmItem>,
    val listener: (FilmItem, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (listType == MainActivity.FILM_LIST) {
            return FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
        } else {
            return FavoriteViewHolder(inflater.inflate(R.layout.favorite_item, parent, false))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is FilmViewHolder) {
            holder.bind(item, position)
            holder.itemView.setOnClickListener { listener(item, position) }
        } else if (holder is FavoriteViewHolder) {
            holder.bind(item)
        }
    }
}