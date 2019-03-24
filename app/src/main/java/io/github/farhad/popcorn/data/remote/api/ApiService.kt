package io.github.farhad.popcorn.data.remote.api

import io.github.farhad.popcorn.data.remote.response.MovieCreditList
import io.github.farhad.popcorn.data.remote.response.PaginatedMovieList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val API_VERSION = "3"
    }
    @GET("$API_VERSION/movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Observable<PaginatedMovieList>

    @GET("$API_VERSION/trending/movie/week")
    fun getWeeklyTrendingMovies(@Query("page") page: Int): Observable<PaginatedMovieList>

    @GET("$API_VERSION/movie/{movieId}/credits")
    fun getMovieCredits(@Path("movieId") movieId: Int): Observable<MovieCreditList>
}