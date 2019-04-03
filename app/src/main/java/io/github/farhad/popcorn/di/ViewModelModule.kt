package io.github.farhad.popcorn.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.farhad.popcorn.ui.movies.TrendingMoviesViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrendingMoviesViewModel::class)
    abstract fun bindMovieViewModel(trendingMoviesViewModel: TrendingMoviesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PopcornViewModelFactory): ViewModelProvider.Factory

}