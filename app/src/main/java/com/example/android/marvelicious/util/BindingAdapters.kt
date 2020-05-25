package com.example.android.marvelicious.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.android.marvelicious.domain.Models

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, image: Models.Image?) {
    image?.let {
        if (image.path.startsWith("http")) {
            val url = generateThumbnailLink(image, MarvelImageTypes.LANDSCAPE_LARGE)
            Glide.with(imageView.context)
                .load(url)
                .centerCrop()
                .placeholder(android.R.drawable.picture_frame)
                .into(imageView)
        }
    }
}