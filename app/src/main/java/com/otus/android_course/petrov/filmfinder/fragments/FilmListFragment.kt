package com.otus.android_course.petrov.filmfinder.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.adapters.FilmAdapter
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.data.filmItems

class FilmListFragment : Fragment() {

    private lateinit var mListener: FilmListClickListener

    interface FilmListClickListener {
        fun onFilmListClick(index: Int)
        fun onFavoriteClick(index: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as FilmListClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnFilmListClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.film_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recyclerViewFilmList).layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.findViewById<RecyclerView>(R.id.recyclerViewFilmList).adapter = FilmAdapter(
            LayoutInflater.from(activity), filmItems, mListener
        )

//todo        recyclerViewFilmList.addItemDecoration(
//            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL).apply {
//                setDrawable(getDrawable(R.drawable.divider)!!)
//            })
    }

    companion object {
        const val TAG = "FilmListFragment"
    }
}