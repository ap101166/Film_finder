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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FilmListFragment(), FilmListFragment.TAG)
            .commit()

        botNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_map -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, FavoritesFragment(), FavoritesFragment.TAG)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.action_dial -> {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    }
                }
                R.id.action_mail -> {
                    Toast.makeText(this, "action_mail", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            ExitDialog().show(supportFragmentManager, "custom")
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }

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

    private fun favoriteAddRemove(index: Int): Boolean {
        // Удаление/добавление в список избранного
        val favItem = FavoriteItem(filmItems[index].caption, filmItems[index].pictureId)
        if (filmItems[index].isFavorite) {
            favoriteFilmItems.remove(favItem)
        } else {
            favoriteFilmItems.add(favItem)
        }
        filmItems[index].isFavorite = !filmItems[index].isFavorite
        // Оповещение recyclerViewFilmList об изменении данных
        findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
        //
        return filmItems[index].isFavorite
    }
}

//Домашнее задание
//Фрагменты и навигация.
//1. Переведите свое приложение на единственную Activity и несколько фрагментов
//2. Для навигации между фрагментами используйте NavigationDrawer или BottomNavigation
//3. Добавьте CoordinatorLayout + CollapsingToolbar на детальный экран фильма
//4. Добавьте Snackbar или Toast, сообщающий об успехе добавления\удаления из избранного
//5. * Добавьте возможность отмены действия в snackbar
