package com.otus.android_course.petrov.filmfinder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    val image: ImageView = itemView.findViewById(R.id.imageViewFilm)
    var favoriteImage : ImageView = itemView.findViewById(R.id.imageFavorite)

    fun bind(item: FilmItem, pos : Int) {
        caption.text = item.caption
        image.setImageResource(item.pictureId)
        favoriteImage.tag = pos
        favoriteImage.setImageResource(if (item.isFavorite) R.drawable.ic_favorite_full else R.drawable.ic_favorite_border)
    }
}