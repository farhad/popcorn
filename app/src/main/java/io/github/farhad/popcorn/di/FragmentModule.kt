package io.github.farhad.popcorn.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.popcorn.ui.movies.MovieListFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment
}