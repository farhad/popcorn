package io.github.farhad.popcorn.data.remote

import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.data.remote.api.ApiService
import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable

/**
 * [apiService] should be injected here
 */
class RemoteDataSource constructor(private val apiService: ApiService) {

    fun getUpcomingMovies(page: Int = 1, transformer: Transformer<List<MovieEntity>?>): Observable<List<MovieEntity>?> {
        return apiService.getUpcomingMovies(page)
            .map { it.movies }
            .compose(transformer)
    }

    fun getTrendingMovies(page: Int = 1, transformer: Transformer<List<MovieEntity>?>): Observable<List<MovieEntity>?> {
        return apiService.getWeeklyTrendingMovies(page)
            .map { it.movies }
            .compose(transformer)
    }

    fun getMoviePerformers(
        movieId: Int,
        transformer: Transformer<List<PerformerEntity>?>
    ): Observable<List<PerformerEntity>?> {
        return apiService.getMovieCredits(movieId)
            .map { it.performers }
            .compose(transformer)
    }

    fun getMovieRoles(movieId: Int, transformer: Transformer<List<RoleEntity>?>): Observable<List<RoleEntity>?> {
        return apiService.getMovieCredits(movieId).map { it.roles }.compose(transformer)
    }
}

