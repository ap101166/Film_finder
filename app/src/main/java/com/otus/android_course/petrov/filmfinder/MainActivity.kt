package com.otus.android_course.petrov.filmfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.data.FilmItem
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

//todo    fun onFavoritesShowClick() {
//        startActivity(Intent(this, FavoritesActivity::class.java))
//    }

    override fun onFilmListClick(item: FilmItem) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FilmDetailsFragment.newInstance(item), FilmDetailsFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun onFavoriteClick(index: Int) {
        // Удаление/добавление в список избранного
        if (filmItems[index].isFavorite) {
            favoriteFilmItems.remove(filmItems[index])
        } else {
            favoriteFilmItems.add(filmItems[index])
        }
        filmItems[index].isFavorite = !filmItems[index].isFavorite
        // Оповещение recyclerViewFilmList об изменении данных
        findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter?.notifyItemChanged(index)
    }
}
