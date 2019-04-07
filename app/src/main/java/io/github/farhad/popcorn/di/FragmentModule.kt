package io.github.farhad.popcorn.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.popcorn.ui.details.MovieDetailsFragment
import io.github.farhad.popcorn.ui.movies.TrendingMoviesFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): TrendingMoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment() : MovieDetailsFragment
}