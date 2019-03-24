package io.github.farhad.popcorn.utils

import android.content.Context

fun loadFileFromAsset(context: Context, path: String): String {
    val file = context.assets.open(path)
    return file.bufferedReader().use { it.readText() }
}