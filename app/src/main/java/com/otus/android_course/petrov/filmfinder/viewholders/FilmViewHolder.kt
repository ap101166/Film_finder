package com.otus.android_course.petrov.filmfinder.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.data.FilmItem
import com.otus.android_course.petrov.filmfinder.R

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    val image: ImageView = itemView.findViewById(R.id.imageViewFilm)
    var favoriteImage : ImageView = itemView.findViewById(R.id.imageFavorite)

    fun bind(item: FilmItem) {
        caption.text = item.caption
        image.setImageResource(item.pictureId)
        favoriteImage.setImageResource(if (item.isFavorite) R.drawable.ic_favorite_full else R.drawable.ic_favorite_border)
    }
}