package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.otus.android_course.petrov.filmfinder.App.Companion.curPageNumber
import com.otus.android_course.petrov.filmfinder.App.Companion.favoriteList
import com.otus.android_course.petrov.filmfinder.App.Companion.filmList
import com.otus.android_course.petrov.filmfinder.App.Companion.netRequestEnabled
import com.otus.android_course.petrov.filmfinder.App.Companion.srvApi
import com.otus.android_course.petrov.filmfinder.MainActivity
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.network.FilmModel
import kotlinx.android.synthetic.main.film_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmListFragment : Fragment() {

    private lateinit var mListener: FilmListClickListener
    private var firstTime = true

    // Интерфейс обработчиков нажатий на элемент списка фильмов
    interface FilmListClickListener {
        fun onFilmListClick(index: Int)
        fun onFavoriteClick(index: Int)
    }

    /**
     * \brief Получение ссылки на интерфейс обработчиков нажатий на элемент списка фильмов
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
        // Получение списка фильмов c сервера при старте приложения
        if (firstTime) {
            loadFilmsFromNet(true)
            firstTime = false
        }
        // Создание recyclerView
        val recyclerViewFilm = view.findViewById<RecyclerView>(R.id.recyclerViewFilmList)
        recyclerViewFilm.apply {
            adapter = FilmAdapter(LayoutInflater.from(activity), filmList, mListener)
            // OnScrollListener для пагинации
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (netRequestEnabled &&
                        ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == filmList.size - 1)
                    ) {
                        netRequestEnabled = false
                        loadFilmsFromNet(false)
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
            netRequestEnabled = false
            filmList.clear()
            recyclerViewFilm.adapter?.notifyDataSetChanged()
            loadFilmsFromNet(true)
        }
    }

    /**
     * \brief Метод для получения списка фильмов с сервера
     */
    fun loadFilmsFromNet(isReload: Boolean) {
        swipeRefreshLayout.isRefreshing = true
        // Перезагрузка списка фильмов с начала
        if (isReload) curPageNumber = 1
        //
        srvApi.getFilmPage(curPageNumber.toString()) // Загрузка текущей страницы
            .enqueue(object : Callback<List<FilmModel>> {
                // Callback на ошибку
                override fun onFailure(call: Call<List<FilmModel>>, t: Throwable) {
                    Toast.makeText(requireActivity(), getString(R.string.str_load_error), Toast.LENGTH_LONG)
                        .show()
                    swipeRefreshLayout.isRefreshing = false
                }

                // Callback на успешное выполнение запроса
                override fun onResponse(
                    call: Call<List<FilmModel>>,
                    response: Response<List<FilmModel>>
                ) {
                    val respSize: Int = response.body()!!.size
                    if (response.isSuccessful && (respSize > 0)) {
                        response.body()
                            ?.forEach { resp ->
                                // Проверка, имется ли загружаемый фильм в списке избранного
                                var isFav = false
                                for (favItem in favoriteList) {
                                    if (favItem.filmId == resp.id) {
                                        isFav = true
                                        // Обновление элементов списка избранного т.к. возможно были изменения
                                        favItem.caption = resp.title
                                        favItem.pictureUrl = resp.image
                                        break
                                    }
                                }
                                // Добавление нового фильма в список
                                filmList.add(
                                    FilmItem(
                                        filmId = resp.id,
                                        caption = resp.title,
                                        description = resp.description,
                                        pictureUrl = resp.image,
                                        isFavorite = isFav
                                    )
                                )
                            }
                        recyclerViewFilmList.adapter?.notifyItemRangeChanged(
                            filmList.size - respSize, respSize
                        )
                        netRequestEnabled = true
                        curPageNumber++
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            })
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}