package io.github.farhad.popcorn.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val releaseDate: Long?,
    val posterUrl: String?,
    val voteAverage: Float?,
    val voteCount: Int?,
    val isAdult: Boolean
) {

    companion object {

        /**
         * empty companion object so that test fixtures can be created
         */
    }
}
