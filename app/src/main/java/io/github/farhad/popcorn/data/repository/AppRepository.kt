package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.db.LocalDataSource
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.remote.RemoteDataSource
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.domain.repository.Repository
import io.github.farhad.popcorn.domain.transformer.IOTransformer
import io.reactivex.Observable
import org.threeten.bp.Instant

/**
 * all the repository instances should be injected here
 */
class AppRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val transformer: EntityTransformer
) : Repository {


    // initializing updatedAts to beginning of timestamp (1970-01-01)
    private var lastTrendingUpdatedAt: Instant = Instant.ofEpochMilli(0)
    private var lastUpcomingUpdatedAt: Instant = Instant.ofEpochMilli(0)

    override fun getUpcomingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {

        val local = localDataSource.getUpcomingMovies(lastUpcomingUpdatedAt, pageSize, IOTransformer())

        val remote = remoteDataSource.getUpcomingMovies(paginationId as Int, IOTransformer())
            .map { movieList ->
                movieList.map { it.copy(category = Category.UPCOMING, updatedAt = Instant.now()) }
                localDataSource.upsertUpcomingMovies(movieList)
                lastUpcomingUpdatedAt = movieList.last().updatedAt
                return@map movieList
            }
            .compose(IOTransformer())

        return local.filter { it.isEmpty() }
            .switchIfEmpty(remote)
            .compose(transformer.MovieTransformer())
    }

    override fun getTrendingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {

        val local = localDataSource.getTrendingMovies(lastTrendingUpdatedAt, pageSize, IOTransformer())

        val remote = remoteDataSource.getTrendingMovies(paginationId as Int, IOTransformer())
            .map { movieList ->
                movieList.map { it.copy(category = Category.TRENDING, updatedAt = Instant.now()) }
                localDataSource.upsertTrendingMovies(movieList)
                lastTrendingUpdatedAt = movieList.last().updatedAt
                return@map movieList
            }
            .compose(IOTransformer())

        return local.filter { it.isEmpty() }
            .switchIfEmpty(remote)
            .compose(transformer.MovieTransformer())
    }

    override fun getMovieCast(movieId: Int): Observable<List<Performer>> {

        val local = localDataSource.getMoviePerformers(movieId, IOTransformer())

        val remote = remoteDataSource.getMoviePerformers(movieId, IOTransformer())
            .map { performerList ->
                performerList.map { it.copy(movieId = movieId) }
                localDataSource.upsertMoviePerformers(performerList)
                return@map performerList
            }
            .compose(IOTransformer())

        return local.filter { it.isEmpty() }
            .switchIfEmpty(remote)
            .compose(transformer.PerformerTransformer())
    }

    override fun getMovieCrew(movieId: Int): Observable<List<Role>> {

        val local = localDataSource.getMovieRoles(movieId, IOTransformer())

        val remote = remoteDataSource.getMovieRoles(movieId, IOTransformer())
            .map { roleList ->
                roleList.map { it.copy(movieId = movieId) }
                localDataSource.upsertMovieRoles(roleList)
                return@map roleList
            }
            .compose(IOTransformer())

        return local.filter { it.isEmpty() }
            .switchIfEmpty(remote)
            .compose(transformer.RoleTransformer())
    }
}