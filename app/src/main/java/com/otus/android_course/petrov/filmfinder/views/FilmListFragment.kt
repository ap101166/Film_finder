package com.otus.android_course.petrov.filmfinder.views

import android.content.Context
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
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.App.Companion.filmList
import com.otus.android_course.petrov.filmfinder.repository.FilmRepository
import com.otus.android_course.petrov.filmfinder.view_models.MainFactory
import com.otus.android_course.petrov.filmfinder.views.recycler_views.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel
import kotlinx.android.synthetic.main.film_list_fragment.*

class FilmListFragment : Fragment() {

    // Интерфейс обработчиков нажатий на элемент списка фильмов
    interface IFilmListClickListeners {
        // Метод для вывода описания фильма
        fun onFilmItemClick(index: Int)
        // Метод для удаления/добавления в список избранного
        fun onFavoriteSignClick(index: Int)
    }

    // Обработчики нажатий на элемент списка фильмов
    private lateinit var mListeners: IFilmListClickListeners

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(),MainFactory(11)).get(MainViewModel::class.java)
    }

    /**
     * \brief Создание фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * \brief Получение ссылки на интерфейс обработчиков нажатий на элемент списка фильмов
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListeners = context as IFilmListClickListeners
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement IFilmListClickListeners")
        }
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

        // Создание recyclerView
        recyclerViewFilmList.apply {
            adapter = FilmAdapter(LayoutInflater.from(activity), filmList, mListeners)
            // OnScroll listener для пагинации
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == filmList.size - 1) {
                        swipeRefreshLayout.isRefreshing = viewModel.onLastVisibleItemPosition()
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
                }
            )
        }

        // Listener на swipe - обновление списка фильмов
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.onSwipeRefresh()
        }

        // Observer на обновление списка фильмов
        viewModel.filmListHasChangedLiveData.observe(requireActivity()) {
            recyclerViewFilmList.adapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        // Observer на ошибку при загрузке списка фильмов
        viewModel.errorLiveData.observe(requireActivity()) { error ->
            if (error == FilmRepository.EMPTY_RESPONSE) {
                // Пришел пустой ответ - нормальная ситуация (размер списка кратен размеру страницы)
            } else {
                // Ошибка при загрузке списка
                Toast.makeText(requireActivity(), getString(R.string.str_load_error) + " $error", Toast.LENGTH_LONG).show()
            }
            swipeRefreshLayout.isRefreshing = false
        }

        // Получение списка фильмов при старте приложения
        swipeRefreshLayout.isRefreshing = viewModel.onAppStart()
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}