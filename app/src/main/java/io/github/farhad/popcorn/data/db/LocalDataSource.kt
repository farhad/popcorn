package io.github.farhad.popcorn.data.db

import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Completable
import io.reactivex.Observable
import org.threeten.bp.Instant
import javax.inject.Inject

class LocalDataSource @Inject constructor(val database: MovieDatabase) {

    fun getUpcomingMovies(
        updatedAfter: Instant,
        itemCount: Int = 20,
        transformer: Transformer<List<MovieEntity>?>
    ): Observable<List<MovieEntity>?> {
        return Observable.just(database.getMovieEntityDao().getUpcomingMovies(updatedAfter, itemCount))
            .compose(transformer)
    }

    fun getTrendingMovies(
        updatedAfter: Instant,
        itemCount: Int = 20,
        transformer: Transformer<List<MovieEntity>?>
    ): Observable<List<MovieEntity>?> {
        return Observable.just(database.getMovieEntityDao().getTrendingMovies(updatedAfter, itemCount))
            .compose(transformer)
    }

    fun getMoviePerformers(
        movieId: Int,
        transformer: Transformer<List<PerformerEntity>?>
    ): Observable<List<PerformerEntity>?> {
        return Observable.just(database.getPerformerEntityDao().getMoviePerformersList(movieId)).compose(transformer)
    }

    fun getMovieRoles(movieId: Int, transformer: Transformer<List<RoleEntity>?>): Observable<List<RoleEntity>?> {
        return Observable.just(database.getRoleEntityDao().getMovieRolesList(movieId)).compose(transformer)
    }

    fun upsertTrendingMovies(movies: List<MovieEntity>) = Completable.fromAction {
        database.getMovieEntityDao().upsert(movies)
    }

    fun upsertUpcomingMovies(movies: List<MovieEntity>) = Completable.fromAction {
        database.getMovieEntityDao().upsert(movies)
    }

    fun upsertMoviePerformers(performers: List<PerformerEntity>) = Completable.fromAction {
        database.getPerformerEntityDao().upsert(performers)
    }

    fun upsertMovieRoles(roles: List<RoleEntity>) = Completable.fromAction {
        database.getRoleEntityDao().upsert(roles)
    }
}