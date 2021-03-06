package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.view_models.MainViewModel

class FilmDetailsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

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
        val filmLst = viewModel.filmListLiveData.value!!
        // Название фильма
        view.findViewById<Toolbar>(R.id.toolbar).title = filmLst[filmIdx].caption
        // Описание фильма
        view.findViewById<TextView>(R.id.textViewDescription).text = filmLst[filmIdx].description
        // Картинка фильма
        val image = view.findViewById<ImageView>(R.id.imageViewFilm)
        Glide.with(image.context)
            .load(filmLst[filmIdx].pictureUrl)
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