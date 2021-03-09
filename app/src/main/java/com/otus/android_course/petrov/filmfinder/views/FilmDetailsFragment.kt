package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.otus.android_course.petrov.filmfinder.App.Companion.filmList
import com.otus.android_course.petrov.filmfinder.R

class FilmDetailsFragment : Fragment() {

    /**
     * \brief Событие создания фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * \brief Создание визуального интерфейса фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    /**
     * \brief Отображение информации о фильме
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmIdx : Int = arguments?.getInt(FILM_INDEX)!!
        // Название фильма
        view.findViewById<Toolbar>(R.id.toolbar).title = filmList[filmIdx].caption
        // Описание фильма
        view.findViewById<TextView>(R.id.textViewDescription).text = filmList[filmIdx].description
        // Картинка фильма
        val image = view.findViewById<ImageView>(R.id.imageViewFilm)
        Glide.with(image.context)
            .load(filmList[filmIdx].pictureUrl)
            .placeholder(R.drawable.ic_load_24dp)
            .error(R.drawable.ic_error_outline_red_24dp)
            .override(image.resources.getDimensionPixelSize(R.dimen.image_size))
            .centerCrop()
            .into(image)
    }

    companion object {
        const val TAG = "FilmDetailsFragment"
        const val FILM_INDEX = "film_index"

        /**
         * \brief Статический метод создания экземпляра FilmDetailsFragment
         */
        fun newInstance(index: Int): FilmDetailsFragment {
            return FilmDetailsFragment().apply {
                arguments = Bundle().apply { putInt(FILM_INDEX, index) }
            }
        }
    }
}