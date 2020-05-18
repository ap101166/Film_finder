package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.allFilmItems

class FilmListFragment : Fragment() {

    private lateinit var mListener: FilmListClickListener

    // Интерфейс обработчиков нажатий на элемент списка фильмов
    interface FilmListClickListener {
        fun onFilmListClick(index: Int)
        fun onFavoriteClick(index: Int)
    }

    /**
    \brief Событие создания фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
    \brief Получение ссылки на интерфейс обработчиков нажатий на элемент списка фильмов
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as FilmListClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FilmListClickListener")
        }
    }

    /**
    \brief Создание визуального интерфейса фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.film_list_fragment, container, false)
    }

    /**
    \brief Создание списка RecyclerView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewFilm = view.findViewById<RecyclerView>(R.id.recyclerViewFilmList)
        recyclerViewFilm.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewFilm.adapter = FilmAdapter(
            LayoutInflater.from(activity), allFilmItems, mListener
        )
        recyclerViewFilm.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(resources.getDrawable(R.drawable.divider, null))
            })
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}