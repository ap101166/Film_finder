package com.otus.android_course.petrov.filmfinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem
import com.otus.android_course.petrov.filmfinder.data.favoriteFilmItems
import com.otus.android_course.petrov.filmfinder.data.filmItems
import com.otus.android_course.petrov.filmfinder.dialogs.ExitDialog
import com.otus.android_course.petrov.filmfinder.fragments.FavoritesFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmDetailsFragment
import com.otus.android_course.petrov.filmfinder.fragments.FilmListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FilmListFragment.FilmListClickListener,
    ExitDialog.NoticeDialogListener {

    /**
    \brief Создание MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Запуск фрагмента со списком фильмов
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FilmListFragment(), FilmListFragment.TAG)
            .commit()

        // Установка обработчиков на BottomNavigation
        botNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Отображение списка фильмов
                R.id.action_film_list -> {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    }
                }
                // Отображение списка избранного
                R.id.action_favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, FavoritesFragment(), FavoritesFragment.TAG)
                        .addToBackStack(null)
                        .commit()
                }
            }
            true
        }
    }

    /**
    \brief Обработчик системной кнопки Назад
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            // Отображение диалога завершения работы приложения
            ExitDialog().show(supportFragmentManager, "ExitDialog")
        }
    }

    /**
    \brief Подтверждение завершения работы приложения в диалоге
     */
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()

    }

    /**
    \brief Метод интерфейса FilmListFragment.FilmListClickListener для вывода описания фильма
     */
    override fun onFilmListClick(index: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                FilmDetailsFragment.newInstance(index),
                FilmDetailsFragment.TAG
            )
            .addToBackStack(null)
            .commit()
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
        val favItem = FavoriteItem(filmItems[index].caption, filmItems[index].pictureId)
        if (filmItems[index].isFavorite) {
            favoriteFilmItems.remove(favItem)
        } else {
            favoriteFilmItems.add(favItem)
        }
        filmItems[index].isFavorite = !filmItems[index].isFavorite
        // Оповещение recyclerView об изменении данных
        findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
        //
        return filmItems[index].isFavorite
    }
}


//1. Переведите свое приложение на единственную Activity и несколько фрагментов
//2. Для навигации между фрагментами используйте NavigationDrawer или BottomNavigation
//3. Добавьте CoordinatorLayout + CollapsingToolbar на детальный экран фильма

