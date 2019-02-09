package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.repository.MovieRepository
import io.reactivex.Observable

class GetMovieCast constructor(
    transformer: Transformer<List<Performer>>,
    private val movieRepository: MovieRepository
) : ObservableUsecase<List<Performer>, GetMovieCast.Params>(transformer) {

    override fun buildUseCase(params: Params?): Observable<List<Performer>> {
        return if (params == null) {
            Observable.error(IllegalArgumentException("movieId parameter cannot be null!"))
        } else {
            movieRepository.getMovieCast(params.movieId)
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