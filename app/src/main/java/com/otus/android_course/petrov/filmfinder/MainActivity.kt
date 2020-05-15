package com.otus.android_course.petrov.filmfinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.data.favoriteFilmItems
import com.otus.android_course.petrov.filmfinder.data.filmItems
import com.otus.android_course.petrov.filmfinder.dialogs.ExitDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FilmAdapter.OnRecyclersClickListener,
    ExitDialog.NoticeDialogListener {

    companion object {
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFilmListRecycler()
        //      initClickListeners()
    }

    private fun initFilmListRecycler() {
        recyclerViewFilmList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewFilmList.adapter = FilmAdapter(
            LayoutInflater.from(this),
            filmItems,
            this as FilmAdapter.OnRecyclersClickListener
        )

        /*     recycler.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                 override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                     if (layoutManager.findLastVisibleItemPosition() == items.size) {
                         repeat(4) {
                             items.add(NewsItem("New item", "----", Color.BLACK))
                         }
                         recycler.adapter?.notifyItemRangeChanged(items.size - 4, 4)
                     }
                 }
             })*/

        recyclerViewFilmList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(getDrawable(R.drawable.divider)!!)
            })
    }

    /*  private fun initClickListeners() {
          findViewById<View>(R.id.addBtn).setOnClickListener {
              items.add(2, NewsItem("New item", "new item subtitle", Color.MAGENTA))
              recyclerView.adapter?.notifyItemInserted(2)
          }

          findViewById<View>(R.id.removeBtn).setOnClickListener {
              items.removeAt(2)
              recyclerView.adapter?.notifyItemRemoved(2)
          }
      }*/

    override fun onBackPressed() {
        ExitDialog().show(supportFragmentManager, "custom")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }

    fun onFavoritesShowClick() {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }

    override fun onFilmListClick(item: FilmItem) {
        startActivity(Intent(this, SecondActivity::class.java).apply {
            putExtra(CAPTION, item.caption)
            putExtra(DESCRIPT, item.description)
            putExtra(PICTURE, item.pictureId)
        })
    }

    override fun onFavoriteClick(index: Int) {
        // Удаление/добавление в список избранного
        if (filmItems[index].isFavorite) {
            favoriteFilmItems.remove(filmItems[index])
        } else {
            favoriteFilmItems.add(filmItems[index])
        }
        filmItems[index].isFavorite = !filmItems[index].isFavorite
        // Оповещение RecyclerViewList об изменении данных
        recyclerViewFilmList.adapter?.notifyItemChanged(index)
    }
}
