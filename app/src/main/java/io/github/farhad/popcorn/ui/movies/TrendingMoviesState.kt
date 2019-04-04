package io.github.farhad.popcorn.ui.movies

import io.github.farhad.popcorn.domain.model.Movie

data class TrendingMoviesState(
    var showLoading: Boolean = true,
    var movies: List<Movie>? = null
)