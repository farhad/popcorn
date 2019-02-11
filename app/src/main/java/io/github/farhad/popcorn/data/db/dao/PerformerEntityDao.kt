package io.github.farhad.popcorn.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.farhad.popcorn.data.entity.PerformerEntity

@Dao
interface PerformerEntityDao : BaseDao<PerformerEntity> {

    @Query("SELECT * FROM Performers WHERE movieId = :movieId")
    fun getMoviePerformersList(movieId: Int): List<PerformerEntity>

    @Query("SELECT COUNT(*) FROM Performers")
    fun count(): Int
}