package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.repository.local_db.Film
import com.otus.android_course.petrov.filmfinder.view_models.FilmsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FilmListFragment.IFilmListClickListeners, ExitDialog.INoticeDialogListener {

    private val viewModel by lazy {
        ViewModelProvider(this).get(FilmsViewModel::class.java)
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
                    supportFragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, FavoritesFragment(), FavoritesFragment.TAG)
                        .addToBackStack(null)
                        .commit()
                }
            }
            true
        }
    }

    /**
     * \brief Метод интерфейса IFilmListClickListeners для вывода описания фильма
     */
    override fun onFilmItemClick(film: Film) {
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, FilmDetailsFragment(film), FilmDetailsFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    /**
     * \brief Метод интерфейса IFilmListClickListeners для удаления/добавления в список избранного
     */
    override fun onFavoriteSignClick(index: Int) {
        val isFavor = viewModel.favoriteSignClick(index)
        val strAddDel = getString(
            if (isFavor) {
                R.string.filmAdded
            } else {
                R.string.filmDeleted
            }
        )
        Snackbar.make(findViewById(R.id.fragmentContainer), strAddDel, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.strCancel)) {
                viewModel.favoriteSignClick(index)
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
