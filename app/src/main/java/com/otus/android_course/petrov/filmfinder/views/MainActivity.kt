package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ExitDialog.INoticeDialogListener {

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

        // Установка Observer{} для вывода описания фильма во фрагмент FilmDetailsFragment
        viewModel.showFilmDetailLiveData.observe(this) { index ->
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

        // Установка Observer{} для отображения удаления/добавления в список избранного
        viewModel.favoritAddRemoveLiveData.observe(this) { index ->
            //
            findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
            //
            val str = getString(
                when (viewModel.filmListLiveData.value!![index].isFavorite) {
                    true -> R.string.filmAdded
                    false -> R.string.filmDeleted
                }
            )
            Snackbar.make(findViewById(R.id.fragmentContainer), str, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.strCancel)) {
                    viewModel.clickListeners.onFavoriteClick(index)
                    findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
                }
                .show()
        }
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
