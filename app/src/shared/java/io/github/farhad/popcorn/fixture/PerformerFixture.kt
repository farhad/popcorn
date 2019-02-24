package io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.domain.model.Performer

/**
 * creates a [Performer] for testing
 */
fun Performer.Companion.create(
    id: String,
    movieId: Int,
    name: String,
    order: Int? = null,
    imageUrl: String? = null,
    character: String? = null
): Performer {

    return Performer(id, movieId, name, order, imageUrl, character)
}