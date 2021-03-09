package com.otus.android_course.petrov.filmfinder.views.recycler_views.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.data.FilmItem

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    private val image: ImageView = itemView.findViewById(R.id.imageViewFilm)
    val favoriteImage: ImageView = itemView.findViewById(R.id.imageFavorite)

    fun bind(item: FilmItem) {
        caption.text = item.caption
        favoriteImage.setImageResource(
            if (item.isFavorite)
                R.drawable.ic_favorite_full
            else
                R.drawable.ic_favorite_border
        )
        Glide.with(image.context)
            .load(item.pictureUrl)
            .placeholder(R.drawable.ic_load_24dp)
            .error(R.drawable.ic_error_outline_red_24dp)
            .override(image.resources.getDimensionPixelSize(R.dimen.image_size))
            .centerCrop()
            .into(image)
    }
}