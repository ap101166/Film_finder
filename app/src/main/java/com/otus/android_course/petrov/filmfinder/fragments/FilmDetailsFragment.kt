package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.otus.android_course.petrov.filmfinder.MainActivity
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.data.filmItems

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
        view.findViewById<Toolbar>(R.id.toolbar).title =
            filmItems[arguments?.getInt(FILM_INDEX)!!].caption
        view.findViewById<TextView>(R.id.textViewDescription).text =
            filmItems[arguments?.getInt(FILM_INDEX)!!].description
        view.findViewById<ImageView>(R.id.imageViewFilm)
            .setImageResource(filmItems[arguments?.getInt(FILM_INDEX)!!].pictureId)
    }

    companion object {
        const val TAG = "FilmDetailsFragment"
        const val FILM_INDEX = "film_index"

        fun newInstance(index: Int): FilmDetailsFragment {
            return FilmDetailsFragment().apply {
                arguments = Bundle().apply { putInt(FILM_INDEX, index) }
            }
        }
    }
}