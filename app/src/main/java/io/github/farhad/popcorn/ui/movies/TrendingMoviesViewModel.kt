package io.github.farhad.popcorn.ui.movies

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.usecase.GetTrendingMovies
import io.github.farhad.popcorn.ui.common.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TrendingMoviesViewModel @Inject constructor(val getTrendingMovies: GetTrendingMovies) : BaseViewModel() {

    fun getTrendingMovies(): Observable<List<Movie>> {

        return getTrendingMovies.execute(GetTrendingMovies.Params(pageSize = 20, paginationId = 1))
    }
}