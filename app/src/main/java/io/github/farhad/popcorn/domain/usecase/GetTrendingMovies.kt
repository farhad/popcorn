package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.repository.Repository
import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable

class GetTrendingMovies constructor(
    transformer: Transformer<List<Movie>>,
    private val repository: Repository
) : ObservableUsecase<List<Movie>, GetTrendingMovies.Params>(transformer) {

    override fun buildUseCase(params: GetTrendingMovies.Params?): Observable<List<Movie>> {
        return if (params == null) {
            Observable.error(IllegalArgumentException("paginationId and pageSize cannot be null!"))
        } else {
            repository.getTrendingMovies(params.paginationId,params.pageSize)
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