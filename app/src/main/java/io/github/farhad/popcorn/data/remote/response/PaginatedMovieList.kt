package io.github.farhad.popcorn.data.remote.response

import com.google.gson.annotations.SerializedName
import io.github.farhad.popcorn.data.entity.MovieEntity

data class PaginatedMovieList(
    @SerializedName("results")
    val movies: List<MovieEntity>?,

    @SerializedName("page")
    val page: Int?,

    @SerializedName("total_pages")
    val totalPages: Int?,

    @SerializedName("total_results")
    val totalResultsCount: Int?
) {
    companion object {
        /**
         * empty companion object so that text fixtures can be created
         */
    }
}