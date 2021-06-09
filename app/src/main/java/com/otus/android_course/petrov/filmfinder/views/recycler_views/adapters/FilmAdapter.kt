package com.otus.android_course.petrov.filmfinder.views.recycler_views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.repository.local_db.Film
import com.otus.android_course.petrov.filmfinder.views.FilmListFragment
import com.otus.android_course.petrov.filmfinder.views.recycler_views.view_holders.FilmViewHolder

class FilmAdapter(
    private val inflater: LayoutInflater,
    private val listeners: FilmListFragment.IFilmListClickListeners?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //
    private val items = ArrayList<Film>()

    /**
     * \brief Обновление локального списка фильмов
     */
    fun updateFilmList(filmLst: List<Film>) {
        items.clear()
        items.addAll(filmLst)
    }

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
            holder.itemView.setOnClickListener { listeners!!.onFilmItemClick(items[position]) }
            holder.favoriteImage.setOnClickListener { listeners!!.onFavoriteSignClick(position) }
        }
    }
}