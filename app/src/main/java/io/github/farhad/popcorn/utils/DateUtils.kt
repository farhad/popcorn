package io.github.farhad.popcorn.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(time: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.US)
    return simpleDateFormat.format(Date(time))
}

fun formatDigits(number : Int) : String {
    return "%,d".format(number)
}