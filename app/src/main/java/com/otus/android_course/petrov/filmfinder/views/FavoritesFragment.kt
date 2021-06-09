package com.otus.android_course.petrov.filmfinder.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.view_models.FilmsViewModel
import com.otus.android_course.petrov.filmfinder.views.recycler_views.adapters.FavoriteAdapter

class FavoritesFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(FilmsViewModel::class.java)
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
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    /**
     * \brief Создание списка RecyclerView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewFavor = view.findViewById<RecyclerView>(R.id.recyclerViewFavorites)
        recyclerViewFavor.apply {
            adapter = FavoriteAdapter(LayoutInflater.from(activity), viewModel.getFavoriteList())
            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(resources.getDrawable(R.drawable.divider, null))
                })
        }
    }

    companion object {
        const val TAG = "FavoritesFragment"
    }
}