package io.github.farhad.popcorn.di

import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.domain.repository.Repository
import io.github.farhad.popcorn.domain.transformer.IOTransformer
import io.github.farhad.popcorn.domain.usecase.GetMovieCast
import io.github.farhad.popcorn.domain.usecase.GetMovieCrew
import io.github.farhad.popcorn.domain.usecase.GetTrendingMovies
import io.github.farhad.popcorn.domain.usecase.GetUpcomingMovies

@Module
class UsecaseModule {

    @ApplicationScope
    @Provides
    fun provideGetTrendingMoviesUsecase(repository: Repository): GetTrendingMovies =
        GetTrendingMovies(IOTransformer(), repository)

    @ApplicationScope
    @Provides
    fun provideGetUpcomingMoviesUsecase(repository: Repository): GetUpcomingMovies =
        GetUpcomingMovies(IOTransformer(), repository)

    @ApplicationScope
    @Provides
    fun provideGetMovieCastsUsecase(repository: Repository): GetMovieCast = GetMovieCast(IOTransformer(), repository)

    @ApplicationScope
    @Provides
    fun provideGetMovieCrewUsecase(repository: Repository): GetMovieCrew = GetMovieCrew(IOTransformer(), repository)
}