package io.github.farhad.popcorn.utils

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ImageLoader @Inject constructor(private val picasso: Picasso) {

    fun load(url: String, imageView: ImageView, callback: (Boolean) -> Unit) {
        picasso.load(url).into(imageView, FetchCallback(callback))
    }

    fun load(url: String, imageView: AppCompatImageView, fadeEffect: Boolean = true) {
        if (fadeEffect)
            picasso.load(url).into(imageView)
        else
            picasso.load(url).noFade().into(imageView)
    }

    fun loadCircular(url: String, imageView: AppCompatImageView) {
        picasso.load(url).transform(RoundedTransformation(30, 10)).into(imageView)
    }

    private class FetchCallback(val delegate: (Boolean) -> Unit) : Callback {
        override fun onSuccess() {
            delegate(true)
        }

        override fun onError(e: Exception?) {
            delegate(false)
        }
    }
}