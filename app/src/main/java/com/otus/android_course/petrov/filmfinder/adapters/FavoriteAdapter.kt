package com.otus.android_course.petrov.filmfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.viewholders.FavoriteViewHolder
import com.otus.android_course.petrov.filmfinder.viewholders.FilmViewHolder

class FavoriteAdapter(
    private val inflater: LayoutInflater,
    private val items: List<FavoriteItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
    \brief Создание объекта ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoriteViewHolder(inflater.inflate(R.layout.favorite_item, parent, false))
    }

    /**
    \brief Получение кол-ва элементов в списке
     */
    override fun getItemCount() = items.size

    /**
    \brief Наполнение объекта ViewHolder данными
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FavoriteViewHolder) {
            holder.bind(items[position])
        }
    }
}