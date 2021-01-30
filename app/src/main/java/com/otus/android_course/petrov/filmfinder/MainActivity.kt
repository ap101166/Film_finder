package com.otus.android_course.petrov.filmfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.App.Companion.filmList
import com.otus.android_course.petrov.filmfinder.App.Companion.favoriteList
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.dialogs.ExitDialog
import com.otus.android_course.petrov.filmfinder.fragments.FavoritesFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmDetailsFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FilmListFragment.FilmListClickListener,
    ExitDialog.NoticeDialogListener {

    /**
     * \brief Создание MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Запуск фрагмента со списком фильмов
        if (supportFragmentManager.findFragmentByTag(FilmListFragment.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
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
                            .setReorderingAllowed(true)
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
    }

    /**
     * \brief Метод интерфейса FilmListFragment.FilmListClickListener для вывода описания фильма
     */
    override fun onFilmListClick(index: Int) {
        if (supportFragmentManager.findFragmentByTag(FilmDetailsFragment.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
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
     * \brief Метод интерфейса FilmListFragment.FilmListClickListener для удаления/добавления в список избранного
     */
    override fun onFavoriteClick(index: Int) {
        //
        val str = favoriteAddRemove(index)
        findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
        //
        Snackbar.make(findViewById(R.id.fragmentContainer), str, Snackbar.LENGTH_LONG)
            .setAction("Отмена") {
                favoriteAddRemove(index)
                findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
            }
            .show()
    }

    /**
     * \brief Удаление/добавление в список избранного
     */
    private fun favoriteAddRemove(filmIdx: Int): String {
        //
        val filmItem = filmList[filmIdx]
        filmItem.isFavorite = !filmItem.isFavorite
        //
        val retStr = if (filmItem.isFavorite) {
            // Добавление в список избранного
            favoriteList.add(FavoriteItem(filmItem.filmId, filmItem.caption, filmItem.pictureUrl))
            "Фильм добавлен"
        } else {
            // Удаление из списка избранного
            for (favItem in favoriteList) {
                if (favItem.filmId == filmItem.filmId) {
                    favoriteList.remove(favItem)
                    break
                }
            }
            "Фильм удален"
        }
        return retStr
    }

    /**
     * \brief Обработчик системной кнопки Назад
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
     * \brief Подтверждение завершения работы приложения в диалоге
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
