package com.otus.android_course.petrov.filmfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.data.allFilmItems
import com.otus.android_course.petrov.filmfinder.data.favoriteItems
import com.otus.android_course.petrov.filmfinder.dialogs.ExitDialog
import com.otus.android_course.petrov.filmfinder.fragments.FavoritesFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmDetailsFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmListFragment
import com.otus.android_course.petrov.filmfinder.network.FilmModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.film_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), FilmListFragment.FilmListClickListener,
    ExitDialog.NoticeDialogListener {

    /**
    \brief Создание MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Запуск фрагмента со списком фильмов
        if (supportFragmentManager.findFragmentByTag(FilmListFragment.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, FilmListFragment(), FilmListFragment.TAG)
                .addToBackStack(LIST_FRAG_NAME)
                .commit()
        }

        // Установка обработчиков на BottomNavigation
        botNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Отображение списка фильмов
                R.id.action_film_list -> {
                    supportFragmentManager.popBackStack(LIST_FRAG_NAME, 0)
                }
                // Отображение списка избранного
                R.id.action_favorites -> {
                    if (supportFragmentManager.findFragmentByTag(FavoritesFragment.TAG) == null) {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragmentContainer,
                                FavoritesFragment(),
                                FavoritesFragment.TAG
                            )
                            .addToBackStack(FAVOR_FRAG_NAME)
                            .commit()
                    }
                }
            }
            true
        }

   //     swipeRefreshLayout.isRefreshing = false
        //
    //    swipeRefreshLayout.setOnRefreshListener { getFilmListFromNet() }

        // Получение списка фильмов c сервера
        getFilmListFromNet()
    }

    //
    fun getFilmListFromNet() {
        //
        allFilmItems.clear()
        //
        App.appInstance.srvApi.getFilms()
            .enqueue(object : Callback<List<FilmModel>?> {
                //
                override fun onFailure(call: Call<List<FilmModel>?>, t: Throwable) {
                    allFilmItems.add(
                        FilmItem(
                            caption = "Ошибка загрузки",   //todo
                            description = "",
                            pictureUrl = "",
                            isFavorite = false
                        )
                    )
                    recyclerViewFilmList.adapter?.notifyDataSetChanged()
                }

                //
                override fun onResponse(
                    call: Call<List<FilmModel>?>,
                    response: Response<List<FilmModel>?>
                ) {
                    if (response.isSuccessful) {
                        response.body()
                            ?.forEach {
                                allFilmItems.add(
                                    FilmItem(
                                        caption = it.title,
                                        description = it.description,
                                        pictureUrl = it.image,
                                        isFavorite = false
                                    )
                                )
                            }
                        recyclerViewFilmList.adapter?.notifyDataSetChanged()
                    }
                }
            })
    }

    /**
    \brief Метод интерфейса FilmListFragment.FilmListClickListener для вывода описания фильма
     */
    override fun onFilmListClick(index: Int) {
        if (supportFragmentManager.findFragmentByTag(FilmDetailsFragment.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    FilmDetailsFragment.newInstance(index),
                    FilmDetailsFragment.TAG
                )
                .addToBackStack(DETAIL_FRAG_NAME)
                .commit()
        }
    }

    /**
    \brief Метод интерфейса FilmListFragment.FilmListClickListener для удаления/добавления в список избранного
     */
    override fun onFavoriteClick(index: Int) {
        //
        val str = if (favoriteAddRemove(index)) {
            "Фильм добавлен"
        } else {
            "Фильм удален"
        }
        Snackbar.make(findViewById(R.id.fragmentContainer), str, Snackbar.LENGTH_LONG)
            .setAction("Отмена") {
                favoriteAddRemove(index)
            }
            .show()
    }

    /**
    \brief Удаление/добавление в список избранного
     */
    private fun favoriteAddRemove(index: Int): Boolean {
        val favItem = FavoriteItem(allFilmItems[index].caption, allFilmItems[index].pictureUrl)
        if (allFilmItems[index].isFavorite) {
            favoriteItems.remove(favItem)
        } else {
            favoriteItems.add(favItem)
        }
        allFilmItems[index].isFavorite = !allFilmItems[index].isFavorite
        // Оповещение recyclerView об изменении данных
        findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
        //
        return allFilmItems[index].isFavorite
    }

    /**
    \brief Обработчик системной кнопки Назад
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            // Возврат к предыдущему фрагменту
            super.onBackPressed()
        } else {
            // Отображение диалога завершения работы приложения
            ExitDialog().show(supportFragmentManager, null)
        }
    }

    /**
    \brief Подтверждение завершения работы приложения в диалоге
     */
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }

    companion object {
        const val LIST_FRAG_NAME = "LIST_FRAG"
        const val DETAIL_FRAG_NAME = "DETAIL_FRAG"
        const val FAVOR_FRAG_NAME = "FAVOR_FRAG"
    }
}
