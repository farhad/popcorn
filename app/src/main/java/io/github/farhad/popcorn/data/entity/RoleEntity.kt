package io.github.farhad.popcorn.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "Roles",
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movieId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class RoleEntity(

    @NonNull
    @PrimaryKey
    @SerializedName("credit_id")
    val id: String,

    @NonNull
    val movieId: Int,

    @NonNull
    @SerializedName("name")
    val name: String,

    @NonNull
    @SerializedName("job")
    val job: String,

    @SerializedName("department")
    val department: String?,

    @SerializedName("profile_path")
    val imageUrl: String?
) {
    companion object {
        /**
         * empty companion object so that test fixtures can be created
         */
    }
}