package com.otus.android_course.petrov.filmfinder.views.recycler_views.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.otus.android_course.petrov.filmfinder.R
import com.otus.android_course.petrov.filmfinder.repository.local_db.FavoriteFilm

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val caption: TextView = itemView.findViewById(R.id.textViewCaption)
    private val image: ImageView = itemView.findViewById(R.id.imageViewFilm)

    fun bind(item: FavoriteFilm) {
        caption.text = item.caption
        Glide.with(image.context)
            .load(item.pictureUrl)
            .placeholder(R.drawable.ic_load_24dp)
            .error(R.drawable.ic_error_outline_red_24dp)
            .override(image.resources.getDimensionPixelSize(R.dimen.image_size))
            .centerCrop()
            .into(image)
    }
}