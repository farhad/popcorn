package io.github.farhad.popcorn.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedString(val type: StringType)

enum class StringType {
    API_KEY,
    API_BASE_URL,
    POSTER_BASE_URL,
    BACKDROP_BASE_URL,
    PERFORMER_ROLE_BASE_URL
}