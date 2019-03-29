package io.github.farhad.popcorn.ui.movies

import androidx.lifecycle.ViewModel
import io.github.farhad.popcorn.data.repository.AppRepository
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.transformer.RelayTransformer
import io.github.farhad.popcorn.domain.usecase.GetUpcomingMovies
import io.reactivex.Observable
import javax.inject.Inject

class MovieViewModel @Inject constructor(val repository: AppRepository) : ViewModel() {

    fun getUpcomingMovies(): Observable<List<Movie>> {

        val useCase = GetUpcomingMovies(RelayTransformer(), repository)

        return useCase.execute(GetUpcomingMovies.Params(pageSize = 20, paginationId = 1))
    }
}