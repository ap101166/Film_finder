package com.otus.android_course.petrov.filmfinder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    val image: ImageView = itemView.findViewById(R.id.imageViewFilm)

    fun bind(item: FilmItem) {
        caption.text = item.caption
        image.setImageResource(item.pictureId)
    }
}