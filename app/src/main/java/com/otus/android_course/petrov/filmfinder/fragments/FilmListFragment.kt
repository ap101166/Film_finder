package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.App.Companion.allFilmItems
import com.otus.android_course.petrov.filmfinder.App.Companion.curPageNumber
import com.otus.android_course.petrov.filmfinder.App.Companion.enableNetRequest
import com.otus.android_course.petrov.filmfinder.MainActivity
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import kotlinx.android.synthetic.main.film_list_fragment.*

class FilmListFragment : Fragment() {

    private lateinit var mListener: FilmListClickListener
    private var firstTime = true

    // Интерфейс обработчиков нажатий на элемент списка фильмов
    interface FilmListClickListener {
        fun onFilmListClick(index: Int)
        fun onFavoriteClick(index: Int)
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
    \brief Событие создания фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // Получение списка фильмов c сервера при старте
        (activity as MainActivity).loadFilmsFromNet()
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
        //
        if (firstTime) swipeRefreshLayout.isRefreshing = true
        firstTime = false
        // Создание recyclerView
        val recyclerViewFilm = view.findViewById<RecyclerView>(R.id.recyclerViewFilmList)
        recyclerViewFilm.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = FilmAdapter(LayoutInflater.from(activity), allFilmItems, mListener)
            // OnScrollListener для пагинации
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (enableNetRequest && ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == allFilmItems.size - 1)) {
                        swipeRefreshLayout.isRefreshing = true
                        (activity as MainActivity).loadFilmsFromNet()
                        enableNetRequest = false
                        Log.d("qqq", "123")
                    }
                }
            })
            //
            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(resources.getDrawable(R.drawable.divider, null))
                })
        }
        // Добавление реакции на swipe - обновление списка фильмов
        swipeRefreshLayout.setOnRefreshListener {
            allFilmItems.clear()
            recyclerViewFilm.adapter?.notifyDataSetChanged()
            curPageNumber = 1
            swipeRefreshLayout.isRefreshing = true
            (activity as MainActivity).loadFilmsFromNet()
        }
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}