package com.example.android.marvelicious.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.android.marvelicious.domain.Models
import timber.log.Timber

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, image: Models.Image) {
    if (image.path.startsWith("http")) {
        val url = generateThumbnailLink(image, MarvelImageTypes.LANDSCAPE_LARGE)
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(android.R.drawable.picture_frame)
            .into(imageView)
    }
}

@BindingAdapter("isNetworkError", "playlist")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, data: Any?) {
    view.visibility = if (data != null) View.GONE else View.VISIBLE

    if (isNetWorkError) {
        view.visibility = View.GONE
    }
}