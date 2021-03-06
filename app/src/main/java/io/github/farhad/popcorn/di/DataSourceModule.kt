package io.github.farhad.popcorn.di

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.data.db.LocalDataSource
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.remote.RemoteDataSource
import io.github.farhad.popcorn.data.remote.api.ApiService
import io.github.farhad.popcorn.data.repository.AppRepository
import io.github.farhad.popcorn.data.repository.EntityMapper
import io.github.farhad.popcorn.data.repository.EntityTransformer
import io.github.farhad.popcorn.domain.repository.Repository

@Module
class DataSourceModule {

    @Provides
    @ApplicationScope
    fun provideAppRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        entityTransformer: EntityTransformer
    ): Repository {
        return AppRepository(remoteDataSource, localDataSource, entityTransformer)
    }

    @Provides
    @ApplicationScope
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource = RemoteDataSource(apiService)

    @Provides
    @ApplicationScope
    fun provideLocalDataSource(database: MovieDatabase, resources: Resources): LocalDataSource =
        LocalDataSource(database, resources)

    @Provides
    @ApplicationScope
    fun provideEntityTransformer(mapper: EntityMapper): EntityTransformer = EntityTransformer(mapper)

    @Provides
    @ApplicationScope
    fun provideEntityMapper(
        @NamedString(StringType.POSTER_BASE_URL) posterBaseUrl: String,
        @NamedString(StringType.BACKDROP_BASE_URL) backDropUrl: String,
        @NamedString(StringType.PERFORMER_ROLE_BASE_URL) performerRoleBaseUrl: String
    ): EntityMapper = EntityMapper(posterBaseUrl, backDropUrl, performerRoleBaseUrl)
}