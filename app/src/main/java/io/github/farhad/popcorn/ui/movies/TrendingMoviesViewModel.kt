package io.github.farhad.popcorn.ui.movies

import io.github.farhad.popcorn.data.repository.AppRepository
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.transformer.RelayTransformer
import io.github.farhad.popcorn.domain.usecase.GetUpcomingMovies
import io.github.farhad.popcorn.ui.common.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TrendingMoviesViewModel @Inject constructor(val repository: AppRepository) : BaseViewModel() {

    fun getUpcomingMovies(): Observable<List<Movie>> {

        val useCase = GetUpcomingMovies(RelayTransformer(), repository)

        return useCase.execute(GetUpcomingMovies.Params(pageSize = 20, paginationId = 1))
    }
}