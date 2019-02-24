package java.io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.data.entity.RoleEntity

fun RoleEntity.Companion.create(
    id: String,
    movieId: Int,
    job: String,
    name: String,
    department: String? = null,
    imageUrl: String? = null
): RoleEntity {
    return RoleEntity(id, movieId, name, job, department, imageUrl)
}