package io.github.farhad.popcorn.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.popcorn.ui.home.HomeActivity

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity
}