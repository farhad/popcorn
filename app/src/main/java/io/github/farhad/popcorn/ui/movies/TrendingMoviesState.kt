package io.github.farhad.popcorn.ui.movies

import io.github.farhad.popcorn.domain.model.Movie

data class TrendingMoviesState(
    var showLoading: Boolean = false,
    var movies: List<Movie>? = null,
    var selectedMovieId: Int? = null
)