package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.domain.repository.Repository
import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class GetMovieCrew constructor(
    transformer: Transformer<List<Role>>,
    private val repository: Repository
) : ObservableUsecase<List<Role>, GetMovieCrew.Params>(transformer) {

    override fun buildUseCase(params: GetMovieCrew.Params?): Observable<List<Role>> {
        return if (params == null) {
            Observable.error(IllegalArgumentException("movieId parameter cannot be null!"))
        } else {
            repository.getMovieCrew(params.movieId)
        }
    }

    data class Params constructor(val movieId: Int) {
        companion object {
            fun forMovie(movieId: Int): Params {
                return Params(movieId)
            }
        }
    }
}
