package com.otus.android_course.petrov.filmfinder.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.R

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    private val image: ImageView = itemView.findViewById(R.id.imageViewFilm)

    fun bind(item: FilmItem) {
        caption.text = item.caption
        image.setImageResource(item.pictureId)
    }
}