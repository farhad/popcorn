package io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.domain.model.Role

/**
 * creates a [Role] for testing
 */
fun Role.Companion.create(
    id: String,
    movieId: Int,
    job: String,
    name: String,
    imageUrl: String? = null,
    department: String? = null
): Role {

    return Role(id, movieId, job, name, imageUrl, department)
}