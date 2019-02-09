package io.github.farhad.popcorn.domain.model

data class Performer(
    val id: String,
    val movieId: Int,
    val name: String,
    val order: Int?,
    val imageUrl: String?,
    val character: String?
) {

    companion object {

        /**
         * empty companion object so that test fixtures can be created
         */
    }
}