package com.otus.android_course.petrov.filmfinder

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

data class FilmAttr(
    val caption: Int,
    val captionView: Int,
    val description: Int,
    val picture: Int
) {}

class MainActivity : AppCompatActivity(), CustomDialog.NoticeDialogListener {

    private val filmsList: List<FilmAttr> = listOf(
        FilmAttr(
            R.string.film_caption_1,
            R.id.TextView1,
            R.string.film_desript_1,
            R.drawable.film_batman
        ),
        FilmAttr(
            R.string.film_caption_2,
            R.id.TextView2,
            R.string.film_desript_2,
            R.drawable.film_pirat
        ),
        FilmAttr(
            R.string.film_caption_3,
            R.id.TextView3,
            R.string.film_desript_3,
            R.drawable.film_plasch_thor
        ),
        FilmAttr(
            R.string.film_caption_4,
            R.id.TextView4,
            R.string.film_desript_4,
            R.drawable.spider_man
        ),
        FilmAttr(
            R.string.film_caption_5,
            R.id.TextView5,
            R.string.film_desript_5,
            R.drawable.film_avatar
        ),
        FilmAttr(
            R.string.film_caption_6,
            R.id.TextView6,
            R.string.film_desript_6,
            R.drawable.film_x_men_wolverine
        )
    )

    companion object {
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"
        const val RET_CHECK_BOX_STATE = "ch_box_state"
        const val RET_TEXT = "ret_text"
        const val FILM_INDEX_KEY = "FILM_INDEX"
        const val THEME_KEY = "THEME"
        const val REQ_CODE = 333
    }

    private var filmIndex = 0
    private var themeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            themeId = getInt(THEME_KEY)
            if (themeId != R.style.AppTheme && themeId != R.style.DarkTheme) {
                themeId = R.style.AppTheme
            }
            setTheme(themeId)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

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

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(FILM_INDEX_KEY, filmIndex)
            putInt(THEME_KEY, themeId)
        })
    }

    // получение ранее сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setFilmIndex(savedInstanceState.getInt(FILM_INDEX_KEY))
    }

    override fun onBackPressed() {
        CustomDialog().show(supportFragmentManager, "custom")
    }

    private fun setFilmIndex(idx: Int) {
        //
        filmIndex = idx
        for (film in filmsList) {
            findViewById<TextView>(film.captionView).setTextColor(resources.getColor(R.color.colorCaption))
        }
        findViewById<TextView>(filmsList[idx].captionView).setTextColor(Color.RED)
    }

    fun onShowDetail(view: View) {
        setFilmIndex(view.tag.toString().toInt())
        val curFilm = filmsList[filmIndex]
        //
        startActivityForResult(Intent(this, SecondActivity::class.java).apply {
            putExtra(CAPTION, curFilm.caption)
            putExtra(DESCRIPT, curFilm.description)
            putExtra(PICTURE, curFilm.picture)
        }, REQ_CODE)
    }

    fun onChangeTheme(view: View) {
        themeId = when (themeId) {
            R.style.AppTheme -> R.style.DarkTheme
            else -> R.style.AppTheme
        }
        recreate()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }
}
