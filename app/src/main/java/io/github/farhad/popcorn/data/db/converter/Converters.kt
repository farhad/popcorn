package io.github.farhad.popcorn.data.db.converter

import androidx.room.TypeConverter
import io.github.farhad.popcorn.data.entity.Category
import org.threeten.bp.Instant
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun categoryToString(category: Category): String = category.toString()

    @TypeConverter
    fun stringToCategory(string: String): Category = Category.from(string)

    @TypeConverter
    fun dateToString(date: Date?): String? {
        date?.let {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return simpleDateFormat.format(date)
        }

        return null
    }

    @TypeConverter
    fun stringToDate(string: String?): Date? {
        string?.let {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return simpleDateFormat.parse(string)
        }

        return null
    }

    @TypeConverter
    fun instantToLong(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun longToInstant(long: Long): Instant? = Instant.ofEpochMilli(long)
}