package io.github.farhad.popcorn.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import org.threeten.bp.Instant

@Dao
interface MovieEntityDao : BaseDao<MovieEntity> {

    @Query(
        """
       SELECT * FROM Movies
       WHERE category = 'upcoming'
       AND updatedAt >= :updatedAfter
       ORDER BY updatedAt ASC
       LIMIT :itemCount
    """
    )
    fun getUpcomingMovies(updatedAfter: Instant, itemCount: Int): List<MovieEntity>

    @Query(
        """
       SELECT * FROM Movies
       WHERE category = 'trending'
       AND updatedAt >= :updatedAfter
       ORDER BY updatedAt ASC
       LIMIT :itemCount
    """
    )
    fun getTrendingMovies(updatedAfter: Instant, itemCount: Int): List<MovieEntity>

    @Query("SELECT COUNT(*) FROM Movies")
    fun count(): Int

    @Query("SELECT COUNT(*) FROM Movies WHERE category = :category")
    fun categoryCount(category: Category): Int

    @Query("DELETE FROM Movies WHERE id=:id")
    fun delete(id: Int)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovie(movieId: Int): MovieEntity?
}