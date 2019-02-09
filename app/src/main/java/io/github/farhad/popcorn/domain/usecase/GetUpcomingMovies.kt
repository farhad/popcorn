package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.repository.MovieRepository
import io.reactivex.Observable

class GetUpcomingMovies constructor(
    transformer: Transformer<List<Movie>>,
    private val movieRepository: MovieRepository
) : ObservableUsecase<List<Movie>, Nothing>(transformer) {

    override fun buildUseCase(params: Nothing?): Observable<List<Movie>> = movieRepository.getUpcomingMovies()
}