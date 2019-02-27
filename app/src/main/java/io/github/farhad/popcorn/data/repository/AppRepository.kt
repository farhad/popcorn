package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.db.LocalDataSource
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

    private var lastUpcomingMovieUpdatedAt: Instant = Instant.now()
    private var lastTrendingMovieUpdatedAt: Instant = Instant.now()

    override fun getUpcomingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {
        val local = localDataSource.getUpcomingMovies(lastTrendingMovieUpdatedAt, pageSize, IOTransformer())
        val remote = remoteDataSource.getUpcomingMovies(paginationId as Int, IOTransformer())

        remote.map { movieList ->
            localDataSource.upsertUpcomingMovies(movieList)
            lastUpcomingMovieUpdatedAt = movieList.last().updatedAt
            movieList
        }.compose(IOTransformer())

        return Observable.concat(local, remote).compose(transformer.MovieTransformer())
    }

    override fun getTrendingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>> {
        val local = localDataSource.getTrendingMovies(lastTrendingMovieUpdatedAt, pageSize, IOTransformer())
        val remote = remoteDataSource.getTrendingMovies(paginationId as Int, IOTransformer())

        remote.map { movieList ->
            localDataSource.upsertTredingMovies(movieList)
            lastTrendingMovieUpdatedAt = movieList.last().updatedAt
            movieList
        }.compose(IOTransformer())

        return Observable.concat(local, remote).compose(transformer.MovieTransformer())
    }

    override fun getMovieCast(movieId: Int): Observable<List<Performer>> {
        val local = localDataSource.getMoviePerformers(movieId, IOTransformer())
        val remote = remoteDataSource.getMoviePerformers(movieId, IOTransformer())

        remote.map { performerList ->
            localDataSource.upsertMoviePerformers(performerList)
            return@map
        }.compose(IOTransformer())

        return Observable.concat(local, remote).compose(transformer.PerformerTransformer())
    }

    override fun getMovieCrew(movieId: Int): Observable<List<Role>> {
        val local = localDataSource.getMovieRoles(movieId, IOTransformer())
        val remote = remoteDataSource.getMovieRoles(movieId, IOTransformer())

        remote.map { roleList ->
            localDataSource.upsertMovieRoles(roleList)
            return@map
        }.compose(IOTransformer())

        return Observable.concat(local, remote).compose(transformer.RoleTransformer())
    }
}