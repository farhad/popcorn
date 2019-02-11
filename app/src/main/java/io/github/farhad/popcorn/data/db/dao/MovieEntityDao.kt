package io.github.farhad.popcorn.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.farhad.popcorn.data.entity.MovieEntity

@Dao
interface MovieEntityDao : BaseDao<MovieEntity> {

    @Query("SELECT * FROM Movies WHERE category = 'upcoming' ")
    fun getUpcomingMovies(): List<MovieEntity>

    @Query("SELECT * FROM Movies WHERE category = 'trending' ")
    fun getTrendingMovies(): List<MovieEntity>

    @Query("SELECT COUNT(*) FROM Movies")
    fun count(): Int

    @Query("DELETE FROM Movies WHERE id=:id")
    fun delete(id: Int)
}