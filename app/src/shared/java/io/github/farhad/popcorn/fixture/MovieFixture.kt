package io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.domain.model.Movie

/**
 * creates a [Movie] for testing
 */
fun Movie.Companion.create(
    id: Int,
    title: String,
    overview: String? = null,
    releaseDate: Long? = null,
    posterUrl: String? = null,
    backdropUrl: String? = null,
    voteAverage: Float? = null,
    voteCount: Int? = null,
    isAdult: Boolean = false
): Movie {
    return Movie(
        id,
        title,
        overview,
        releaseDate,
        posterUrl,
        backdropUrl,
        voteAverage,
        voteCount,
        isAdult
    )
}