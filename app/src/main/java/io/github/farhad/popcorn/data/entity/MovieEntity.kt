package io.github.farhad.popcorn.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Movies")
data class MovieEntity(

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    val id: Int,

    @NonNull
    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("release_date")
    val releaseDate: Long?,

    @SerializedName("poster_path")
    val posterUrl: String?,

    @SerializedName("vote_average")
    val voteAverage: Float?,

    @SerializedName("vote_count")
    val voteCount: Int?,

    @SerializedName("adult")
    val isAdult: Boolean,

    val category: Category
) {
    companion object {
        /**
         * empty companion object so that test fixtures can be created
         */
    }
}