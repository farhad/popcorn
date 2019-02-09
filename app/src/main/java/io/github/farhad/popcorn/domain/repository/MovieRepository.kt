package io.github.farhad.popcorn.domain.repository

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.reactivex.Observable

interface MovieRepository {

    fun getUpcomingMovies(): Observable<List<Movie>>

    fun getTrendingMovies(): Observable<List<Movie>>

    /**
     * implementations should return Observable of empty list
     * when either the movieId is not found, or the performers list of an existing movie is empty
     */
    fun getMovieCast(movieId: Int): Observable<List<Performer>>

    /**
     * implementation should return Observable of empty list
     * when either the movieId is not found, or the roles list of an existing movie is empty
     */
    fun getMovieCrew(movieId: Int): Observable<List<Role>>
}