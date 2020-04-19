package com.otus.android_course.petrov.filmfinder

import android.R.id.message
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //
        findViewById<TextView>(R.id.textViewCaption).setText(
            intent.extras?.getInt(MainActivity.CAPTION) ?: 0
        )
        findViewById<TextView>(R.id.textViewDescription).setText(
            intent.extras?.getInt(MainActivity.DESCRIPT) ?: 0
        )
        findViewById<ImageView>(R.id.imageViewFilm).setImageResource(
            intent.extras?.getInt(MainActivity.PICTURE) ?: 0
        )
    }

    // Приглашение друга посмотреть фильм
    fun onInviteFriend(view: View) {
        // Создание сообщения
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Посмотри фильм " + findViewById<TextView>(R.id.textViewCaption).text
            )
        }
        // Отправка сообщения
        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(sendIntent)
        }
    }

    fun onSendResp(view: View) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(
                MainActivity.RET_CHECK_BOX_STATE,
                findViewById<CheckBox>(R.id.checkBox).isChecked
            )
            putExtra(MainActivity.RET_TEXT, findViewById<TextView>(R.id.editTextComment).text)
        })
        finish()
    }
}