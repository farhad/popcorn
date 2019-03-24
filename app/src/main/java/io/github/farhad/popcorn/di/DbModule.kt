package io.github.farhad.popcorn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.db.dao.MovieEntityDao
import io.github.farhad.popcorn.data.db.dao.PerformerEntityDao
import io.github.farhad.popcorn.data.db.dao.RoleEntityDao

@Module
class DbModule {

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Context): MovieDatabase = MovieDatabase.create(context)

    @Provides
    fun provideMovieEntityDao(database: MovieDatabase): MovieEntityDao = database.getMovieEntityDao()

    @Provides
    fun providePerformerEntityDao(database: MovieDatabase): PerformerEntityDao = database.getPerformerEntityDao()

    @Provides
    fun provideRoleEntityDao(database: MovieDatabase): RoleEntityDao = database.getRoleEntityDao()
}