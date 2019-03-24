package io.github.farhad.popcorn.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedString(val type: StringType)

enum class StringType {
    API_KEY,
    BASE_URL
}