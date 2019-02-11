package io.github.farhad.popcorn.data.db.converter

import androidx.room.TypeConverter
import io.github.farhad.popcorn.data.entity.Category

class Converters {

    @TypeConverter
    fun categoryToString(category: Category): String = category.toString()

    @TypeConverter
    fun stringToCategory(string: String): Category = Category.from(string)
}