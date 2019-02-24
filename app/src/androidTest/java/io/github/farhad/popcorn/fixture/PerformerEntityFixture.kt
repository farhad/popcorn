package java.io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.data.entity.PerformerEntity

fun PerformerEntity.Companion.create(
    id: String,
    movieId: Int,
    name: String,
    order: Int? = null,
    character: String? = null,
    imageUrl: String? = null
): PerformerEntity {

    return PerformerEntity(id, movieId, name, order, character, imageUrl)
}