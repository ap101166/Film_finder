package com.otus.android_course.petrov.filmfinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.favoriteFilmItems
import com.otus.android_course.petrov.filmfinder.data.filmItems
import com.otus.android_course.petrov.filmfinder.dialogs.ExitDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ExitDialog.NoticeDialogListener {

    companion object {
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"
        const val REQ_CODE = 333
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListRecycler()
        //      initClickListeners()
    }

    private fun initListRecycler() {
        recyclerViewList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewList.adapter =
            FilmAdapter(
                LayoutInflater.from(this),
                filmItems
            ) { filmItem, _ ->
                startActivityForResult(Intent(this, SecondActivity::class.java).apply {
                    putExtra(CAPTION, filmItem.caption)
                    putExtra(DESCRIPT, filmItem.description)
                    putExtra(PICTURE, filmItem.pictureId)
                }, REQ_CODE)
            }
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

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
        recyclerViewList.addItemDecoration(itemDecoration)
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
        ExitDialog()
            .show(supportFragmentManager, "custom")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }

    fun onFavoriteAddRemoveClick(view: View) {
        // Индекс фильма в основном списке
        val fIndex = view.tag.toString().toInt()
        //
        filmItems[fIndex].isFavorite.let {
            if (it) {
                // Удаление из списка избранного
                favoriteFilmItems.remove(
                    filmItems[fIndex]
                )
            } else {
                // Добавление в список избранного
                favoriteFilmItems.add(
                    filmItems[fIndex]
                )
            }
            filmItems[fIndex].isFavorite = !it
        }
        // Оповещение RecyclerViewList об изменении данных
        recyclerViewList.adapter?.notifyItemChanged(fIndex)
    }

    fun onFavoritesShowClick(view: View) {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }
}
