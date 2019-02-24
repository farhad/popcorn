package io.github.farhad.popcorn.domain.repository

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.reactivex.Observable

/**
 * This is the general contract for a movie repository.
 * The operations are pretty self-explanatory, except for the input parameters of the [getUpcomingMovies] and
 * [getTrendingMovies]. The [paginationId] and [pageSize] has been added, because the pagination is a
 * cross-cutting concern and can be addressed in the gateways. So it does makes sense to add support for pagination
 * in repository contract and usecases.
 * The [paginationId] is a general name, intended to cover paged and itemKey paging.
 * More information [https://craftsmanshipcounts.com/clean-architecture-sorting-filtering-paging/]
 */
interface Repository {

    fun getUpcomingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>>

    fun getTrendingMovies(paginationId: Any, pageSize: Int): Observable<List<Movie>>

    fun getMovieCast(movieId: Int): Observable<List<Performer>>

    fun getMovieCrew(movieId: Int): Observable<List<Role>>
}