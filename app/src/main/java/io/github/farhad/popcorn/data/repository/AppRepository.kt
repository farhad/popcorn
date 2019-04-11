package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.db.LocalDataSource
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.remote.RemoteDataSource
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.domain.repository.Repository
import io.github.farhad.popcorn.domain.transformer.IOTransformer
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.Instant
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val transformer: EntityTransformer
) : Repository {

    private var lastTrendingUpdatedAt: Instant
    private var lastUpcomingUpdatedAt: Instant

    init {
        lastTrendingUpdatedAt = Instant.ofEpochMilli(0)
        lastUpcomingUpdatedAt = Instant.ofEpochMilli(0)
    }

    override fun getUpcomingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {

        val local = localDataSource.getUpcomingMovies(lastUpcomingUpdatedAt, pageSize, IOTransformer()).doOnNext {
            if (!it.isNullOrEmpty()) lastUpcomingUpdatedAt = it.last().updatedAt
        }

        val remote = remoteDataSource.getUpcomingMovies(paginationId as Int, IOTransformer()).doOnNext {
            if (!it.isNullOrEmpty()) {
                /**
                 * replaced map with iterative loop, because the map executes parallel and many items share the same updatedAt
                 * value and we lose the order of items when showing offline data from database
                 */
                val movies = mutableListOf<MovieEntity>()
                for (i in 0 until it.size) {
                    movies.add(
                        it[i].copy(
                            category = Category.UPCOMING,
                            updatedAt = Instant.now().plusMillis(i.toLong())
                        )
                    )
                }
                localDataSource.upsertUpcomingMovies(movies).subscribe()
                lastUpcomingUpdatedAt = it.last().updatedAt
            }
        }

        return remote.onErrorResumeNext(local).compose(transformer.MovieTransformer())
    }

    override fun getTrendingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {

        val local = localDataSource.getTrendingMovies(lastTrendingUpdatedAt, pageSize, IOTransformer()).doOnNext {
            if (!it.isNullOrEmpty()) lastTrendingUpdatedAt = it.last().updatedAt
        }

        val remote = remoteDataSource.getTrendingMovies(paginationId as Int, IOTransformer()).doOnNext {
            if (!it.isNullOrEmpty()) {
                /**
                 * replaced map with iterative loop, because the map executes parallel and many items share the same updatedAt
                 * value and we lose the order of items when showing offline data from database
                 */
                val movies = mutableListOf<MovieEntity>()
                for (i in 0 until it.size) {
                    movies.add(
                        it[i].copy(
                            category = Category.TRENDING,
                            updatedAt = Instant.now().plusMillis(i.toLong())
                        )
                    )
                }

                localDataSource.upsertTrendingMovies(movies).subscribe()
                lastTrendingUpdatedAt = movies.last().updatedAt
            }
        }

        return remote.onErrorResumeNext(local).compose(transformer.MovieTransformer())
    }

    override fun getMovieCast(movieId: Int): Observable<List<Performer>> {

        val local = localDataSource.getMoviePerformers(movieId, IOTransformer())

        val remote = remoteDataSource.getMoviePerformers(movieId, IOTransformer()).doOnNext {
            if (it != null) {
                val performers = it.map { item -> item.copy(movieId = movieId) }
                localDataSource.upsertMoviePerformers(performers)
            }
        }

        return remote.onErrorResumeNext(local).compose(transformer.PerformerTransformer())
    }

    override fun getMovieCrew(movieId: Int): Observable<List<Role>> {

        val local = localDataSource.getMovieRoles(movieId, IOTransformer())

        val remote = remoteDataSource.getMovieRoles(movieId, IOTransformer()).doOnNext {
            if (it != null) {
                val roles = it.map { item -> item.copy(movieId = movieId) }
                localDataSource.upsertMovieRoles(roles)
            }
        }

        return remote.onErrorResumeNext(local).compose(transformer.RoleTransformer())
    }

    override fun getMovieInfo(movieId: Int): Single<Movie> {
        return localDataSource.getMovie(movieId).map { transformer.mapper.toMovie(it) }
    }
}