package io.github.farhad.popcorn.data.db

import android.content.res.Resources
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.Instant
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val database: MovieDatabase,
    private val resources: Resources
) {

    fun getUpcomingMovies(
        updatedAfter: Instant = Instant.now(),
        itemCount: Int = 20,
        transformer: Transformer<List<MovieEntity>?>
    ): Observable<List<MovieEntity>?> {

        return Observable.create<List<MovieEntity>?> {
            try {
                val movies = database.getMovieEntityDao().getUpcomingMovies(updatedAfter, itemCount)
                if (movies.isNullOrEmpty()) {
                    it.onError(Exception(resources.getString(R.string.error_data_no_movies)))
                } else {
                    it.onNext(movies)
                    it.onComplete()
                }
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }.compose(transformer)
    }

    fun getTrendingMovies(
        updatedAfter: Instant = Instant.now(),
        itemCount: Int = 20,
        transformer: Transformer<List<MovieEntity>?>
    ): Observable<List<MovieEntity>?> {
        return Observable.create<List<MovieEntity>?> {
            try {
                val movies = database.getMovieEntityDao().getTrendingMovies(updatedAfter, itemCount)
                if (movies.isNullOrEmpty()) {
                    it.onError(Exception(resources.getString(R.string.error_data_no_movies)))
                } else {
                    it.onNext(movies)
                    it.onComplete()
                }

            } catch (ex: Exception) {
                it.onError(ex)
            }
        }.compose(transformer)
    }

    fun getMoviePerformers(
        movieId: Int,
        transformer: Transformer<List<PerformerEntity>?>
    ): Observable<List<PerformerEntity>?> {

        return Observable.create<List<PerformerEntity>?> {
            try {
                val performers = database.getPerformerEntityDao().getMoviePerformersList(movieId)
                if (performers.isNullOrEmpty()) {
                    it.onError(Exception(resources.getString(R.string.error_data_no_movie_credits)))
                } else {
                    it.onNext(performers)
                    it.onComplete()
                }

            } catch (ex: Exception) {
                it.onError(ex)
            }
        }.compose(transformer)
    }

    fun getMovieRoles(movieId: Int, transformer: Transformer<List<RoleEntity>?>): Observable<List<RoleEntity>?> {
        return Observable.create<List<RoleEntity>?> {
            try {
                val roles = database.getRoleEntityDao().getMovieRolesList(movieId)
                if (roles.isNullOrEmpty()) {
                    it.onError(Exception(resources.getString(R.string.error_data_no_movie_credits)))
                } else {
                    it.onNext(roles)
                    it.onComplete()
                }
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }.compose(transformer)
    }

    fun upsertTrendingMovies(movies: List<MovieEntity>): Completable = Completable.fromAction {
        database.getMovieEntityDao().upsert(movies)
    }

    fun upsertUpcomingMovies(movies: List<MovieEntity>): Completable = Completable.fromAction {
        database.getMovieEntityDao().upsert(movies)
    }

    fun upsertMoviePerformers(performers: List<PerformerEntity>): Completable = Completable.fromAction {
        database.getPerformerEntityDao().upsert(performers)
    }

    fun upsertMovieRoles(roles: List<RoleEntity>) = Completable.fromAction {
        database.getRoleEntityDao().upsert(roles)
    }

    fun getMovie(movieId: Int): Single<MovieEntity?> {
        return Single.create {
            val movie = database.getMovieEntityDao().getMovie(movieId)
            if (movie == null)
                it.onError(IllegalArgumentException("movie not found"))
            else
                it.onSuccess(movie)
        }
    }
}