package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.views.recycler_views.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel
import kotlinx.android.synthetic.main.film_list_fragment.*

class FilmListFragment : Fragment() {

//    private var firstTime = true
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    /**
     * \brief Создание фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * \brief Создание визуального интерфейса фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.film_list_fragment, container, false)
    }

    /**
     * \brief Добавление обработчиков событий визуальных компонентов
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        // Получение списка фильмов c сервера при старте приложения
//        if (firstTime) {
//            viewModel.getFilmList(true)
//            swipeRefreshLayout.isRefreshing = true
//            firstTime = false
//        }
        // Создание recyclerView
        val recyclerViewFilm = view.findViewById<RecyclerView>(R.id.recyclerViewFilmList)
        val filmAdapter = FilmAdapter(LayoutInflater.from(activity), App.filmList, viewModel.filmClickListeners)
        recyclerViewFilm.apply {
            adapter = filmAdapter
            // OnScrollListener для пагинации
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (App.netRequestEnabled &&
                        ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == App.filmList.size - 1)
                    ) {
                        App.netRequestEnabled = false
                        viewModel.getFilmList(false)
                        swipeRefreshLayout.isRefreshing = true
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
            viewModel.onSwipeRefresh()
        }

        // Observer на обновление списка фильмов
        viewModel.filmListChangeLiveData.observe(requireActivity()) {
            filmAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        // Observer на ошибку при загрузке списка фильмов
        viewModel.errorLiveData.observe(requireActivity()) { error ->
            Toast.makeText(requireActivity(), getString(R.string.str_load_error) + " $error", Toast.LENGTH_LONG)
                .show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}