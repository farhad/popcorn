package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.repository.Repository
import io.reactivex.Observable

class GetUpcomingMovies constructor(
    transformer: Transformer<List<Movie>>,
    private val repository: Repository
) : ObservableUsecase<List<Movie>, GetUpcomingMovies.Params>(transformer) {

    override fun buildUseCase(params: GetUpcomingMovies.Params?): Observable<List<Movie>> {
        return if (params == null) {
            Observable.error(IllegalArgumentException("paginationId and pageSize cannot be null!"))
        } else {
            repository.getUpcomingMovies(params.paginationId, params.pageSize)
        }
    }

    data class Params constructor(val paginationId: Int, val pageSize: Int) {
        companion object {
            fun forBatch(paginationId: Int, pageSize: Int): Params {
                return Params(paginationId, pageSize)
            }
        }
    }
}

