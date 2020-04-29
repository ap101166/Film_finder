package com.otus.android_course.petrov.filmfinder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), CustomDialog.NoticeDialogListener {

    companion object {
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"
        const val RET_CHECK_BOX_STATE = "ch_box_state"
        const val RET_TEXT = "ret_text"
        const val REQ_CODE = 333
        const val FILM_LIST = 1
        const val FAVORITE_LIST = 2
    }

    lateinit private var recycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        //      initClickListeners()
    }

    private fun initRecycler() {
        recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = layoutManager
        recycler.adapter = FilmAdapter(FILM_LIST, LayoutInflater.from(this), filmItems) { filmItem, position ->
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
        recycler.addItemDecoration(itemDecoration)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("FF_TAG", data?.extras?.getBoolean(RET_CHECK_BOX_STATE).toString())
                Log.d("FF_TAG", data?.extras?.get(RET_TEXT).toString())
            } else {
                Log.d("FF_TAG", "Ошибка!")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        CustomDialog().show(supportFragmentManager, "custom")
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
                favoriteFilmItems.remove(filmItems[fIndex])
            } else {
                // Добавление в список избранного
                favoriteFilmItems.add(filmItems[fIndex])
            }
            filmItems[fIndex].isFavorite = !it
        }
        // Оповещение RecyclerView об изменении данных
        recycler.adapter?.notifyItemChanged(fIndex)
    }

    fun onFavoritesShowClick(view: View) {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }
}
