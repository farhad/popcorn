package io.github.farhad.popcorn.data.remote.api

import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter : JsonSerializer<Date>, JsonDeserializer<Date> {

    override fun serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.US)
        return context.serialize(simpleDateFormat.format(Date()))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return simpleDateFormat.parse(json.asString)
    }
}