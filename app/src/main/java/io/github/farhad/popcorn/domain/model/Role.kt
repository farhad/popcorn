package io.github.farhad.popcorn.domain.model

data class Role(
    val id: String,
    val movieId: Int,
    val job: String,
    val name: String,
    val imageUrl: String?,
    val department: String?
) {

    companion object {

        /**
         * empty companion object so that test fixtures can be created
         */
    }
}