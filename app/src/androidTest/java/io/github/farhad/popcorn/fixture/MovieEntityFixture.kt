package io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import java.util.*

/**
 * creates a [MovieEntity] for testing
 */
fun MovieEntity.Companion.create(
    id: Int,
    title: String,
    overview: String? = null,
    releaseDate: Date? = null,
    posterUrl: String? = null,
    voteAverage: Float? = null,
    voteCount: Int? = null,
    isAdult: Boolean = false,
    category: Category
): MovieEntity {

    return MovieEntity(
        id,
        title,
        overview,
        releaseDate,
        posterUrl,
        voteAverage,
        voteCount,
        isAdult,
        category
    )
}