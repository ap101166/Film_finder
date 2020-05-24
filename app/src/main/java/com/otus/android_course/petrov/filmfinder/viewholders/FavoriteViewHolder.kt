package com.otus.android_course.petrov.filmfinder.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FavoriteItem

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    private val image: ImageView = itemView.findViewById(R.id.imageViewFilm)

    fun bind(item: FavoriteItem) {
        caption.text = item.caption
//        image.setImageResource(item.pictureId)
    }
}