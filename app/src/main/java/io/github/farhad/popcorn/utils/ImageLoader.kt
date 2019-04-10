package io.github.farhad.popcorn.utils

import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ImageLoader @Inject constructor(private val picasso: Picasso) {

    fun load(url: String, imageView: AppCompatImageView, fadeEffect: Boolean = true) {
        if (fadeEffect)
            picasso.load(url).into(imageView)
        else
            picasso.load(url).noFade().into(imageView)
    }

    fun load(url: String, imageView: AppCompatImageView, @DrawableRes placeholder: Int) {
        picasso.load(url).placeholder(placeholder).into(imageView)
    }

    fun loadCircular(url: String, imageView: AppCompatImageView, @DrawableRes placeholder: Int) {
        picasso.load(url).transform(CircularTransformer()).placeholder(placeholder).into(imageView)
    }
}