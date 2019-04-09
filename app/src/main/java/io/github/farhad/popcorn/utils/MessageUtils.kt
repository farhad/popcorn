package io.github.farhad.popcorn.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun show(message: String, anchorView: View) {
    Snackbar.make(anchorView, message, Snackbar.LENGTH_LONG).show()
}