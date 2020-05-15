package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.otus.android_course.petrov.filmfinder.MainActivity
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem

class FilmDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        view.findViewById<TextView>(R.id.textViewCaption).text = arguments?.getString(CAPTION)!!
        view.findViewById<TextView>(R.id.textViewDescription).text = arguments?.getString(DESCRIPT)!!
        view.findViewById<ImageView>(R.id.imageViewFilm).setImageResource(arguments?.getInt(PICTURE)!!)
    }

    // Приглашение друга посмотреть фильм
    fun onInviteFriend(view: View) {
//todo        // Создание сообщения
//        val sendIntent = Intent(Intent.ACTION_SEND).apply {
//            type = "text/plain"
//            putExtra(
//                Intent.EXTRA_TEXT,
//                "Посмотри фильм " + textViewCaption.text
//            )
//        }
//        // Отправка сообщения
//        if (sendIntent.resolveActivity(packageManager) != null) {
//            startActivity(sendIntent)
//        }
    }

    companion object {
        const val TAG = "FilmDetailsFragment"
        const val CAPTION = "caption"
        const val DESCRIPT = "description"
        const val PICTURE = "picture"

        fun newInstance(item: FilmItem) : FilmDetailsFragment {
            return FilmDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(CAPTION, item.caption)
                    putString(DESCRIPT, item.description)
                    putInt(PICTURE, item.pictureId)
                }
            }
        }
    }
}