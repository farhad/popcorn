package io.github.farhad.popcorn.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.PopcornApp

@Module
class AppModule {

    @Provides
    fun provideContext(app: PopcornApp): Context = app.applicationContext

    @Provides
    fun provideResources(context: Context): Resources = context.resources
}