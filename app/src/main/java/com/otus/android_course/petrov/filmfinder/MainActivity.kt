package com.otus.android_course.petrov.filmfinder

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"
        const val RET_CHECK_BOX_STATE = "ch_box_state"
        const val RET_TEXT = "ret_text"
    }

    private val arrCaptions =
        arrayOf(
            R.id.TextView1,
            R.id.TextView2,
            R.id.TextView3,
            R.id.TextView4
        )
    private val arrStrCaptions =
        arrayOf(
            R.string.film_caption_1,
            R.string.film_caption_2,
            R.string.film_caption_3,
            R.string.film_caption_4
        )
    private val arrStrDescriptions =
        arrayOf(
            R.string.film_desript_1,
            R.string.film_desript_2,
            R.string.film_desript_3,
            R.string.film_desript_4
        )
    private val arrImages =
        arrayOf(
            R.drawable.film_batman,
            R.drawable.film_pirat,
            R.drawable.film_plasch_thor,
            R.drawable.spider_man
        )

    private var filmIndex = 0
    private val FILM_INDEX_KEY = "FILM_INDEX"
    private val REQ_CODE = 333

    override fun onCreate(savedInstanceState: Bundle?) {
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
        super.onSaveInstanceState(outState.apply { putInt(FILM_INDEX_KEY, filmIndex) })
    }

    // получение ранее сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setFilmIndex(savedInstanceState.getInt(FILM_INDEX_KEY))
    }

    fun setFilmIndex(idx: Int) {
        //
        filmIndex = idx
        for (views in arrCaptions) {
            findViewById<TextView>(views).setTextColor(Color.BLACK)
        }
        findViewById<TextView>(arrCaptions[idx]).setTextColor(Color.GREEN)
    }

    fun onShowDetail(view: View) {
        setFilmIndex(view.tag.toString().toInt())
        //
        startActivityForResult(Intent(this, SecondActivity::class.java).apply {
            putExtra(CAPTION, arrStrCaptions[filmIndex])
            putExtra(DESCRIPT, arrStrDescriptions[filmIndex])
            putExtra(PICTURE, arrImages[filmIndex])
        }, REQ_CODE)
    }
}
