package com.otus.android_course.petrov.filmfinder.views.recycler_views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListener
import com.otus.android_course.petrov.filmfinder.views.FilmListFragment
import com.otus.android_course.petrov.filmfinder.views.recycler_views.view_holders.FilmViewHolder

class FilmAdapter(
    private val inflater: LayoutInflater,
    private val items: List<FilmItem>,
    private val listener: IFilmListClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * \brief Создание объекта ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(inflater.inflate(R.layout.film_item, parent, false))
    }

    /**
     * \brief Получение кол-ва элементов в списке
     */
    override fun getItemCount() = items.size

    /**
     * \brief Наполнение объекта ViewHolder данными
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is FilmViewHolder) {
            holder.bind(item)
            // Установка обработчиков нажатия на весь элемент и на Избранное
            holder.itemView.setOnClickListener { listener.onFilmListClick(position) }
            holder.favoriteImage.setOnClickListener { listener.onFavoriteClick(position) }
        }
    }
}