package io.github.farhad.popcorn.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedOkHttpClient(val type: ClientType)

enum class ClientType {
    CACHE_ON,
    CACHE_OFF
}