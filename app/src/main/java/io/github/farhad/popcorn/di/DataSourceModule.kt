package io.github.farhad.popcorn.di

import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.data.db.LocalDataSource
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.remote.RemoteDataSource
import io.github.farhad.popcorn.data.remote.api.ApiService
import io.github.farhad.popcorn.data.repository.EntityTransformer

@Module
class DataSourceModule {

    @Provides
    @ApplicationScope
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource = RemoteDataSource(apiService)

    @Provides
    @ApplicationScope
    fun provideLocalDataSource(database: MovieDatabase): LocalDataSource = LocalDataSource(database)

    @Provides
    @ApplicationScope
    fun provideEntityTransformer(): EntityTransformer = EntityTransformer()
}