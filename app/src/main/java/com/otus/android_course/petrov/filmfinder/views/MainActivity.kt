package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.App
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.interfaces.IFilmListClickListeners
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.film_list_fragment.*

class MainActivity : AppCompatActivity(), IFilmListClickListeners, ExitDialog.INoticeDialogListener {

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

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
                            .replace(R.id.fragmentContainer, FavoritesFragment(), FavoritesFragment.TAG)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            true
        }
    }

    /**
     * \brief Метод интерфейса IFilmListClickListeners для вывода описания фильма
     */
    override fun onFilmItemClick(index: Int) {
        if (supportFragmentManager.findFragmentByTag(FilmDetailsFragment.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.fragmentContainer,
                    FilmDetailsFragment.newInstance(index),
                    FilmDetailsFragment.TAG
                )
                .addToBackStack(null)
                .commit()
        }
    }

    /**
     * \brief Метод интерфейса IFilmListClickListeners для удаления/добавления в список избранного
     */
    override fun onFavoriteSignClick(index: Int) {
        //
        viewModel.favoriteSignClick(index)
        recyclerViewFilmList.adapter!!.notifyItemChanged(index)
        //
        val str = getString(
            if (App.filmList[index].isFavorite) {
                R.string.filmAdded
            } else {
                R.string.filmDeleted
            }
        )
        Snackbar.make(findViewById(R.id.fragmentContainer), str, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.strCancel)) {
                onFavoriteSignClick(index)
            }
            .show()
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
    }
}
