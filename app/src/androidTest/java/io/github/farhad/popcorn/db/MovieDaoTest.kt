package io.github.farhad.popcorn.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.db.dao.MovieEntityDao
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.fixture.create
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    companion object {
        const val NON_EXISTENT_MOVIE_ID = 404
    }

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieEntityDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        movieDao = movieDatabase.getMovieEntityDao()
    }

    @After
    fun tearDown() {
        movieDatabase.clearAllTables()
        movieDatabase.close()
    }

    @Test
    fun movieDatabase_has_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        // Act
        movieDao.insert(trendingMovieOne)

        // Assert
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_insert_inserts_single_trending_movie_row_into_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        // Act
        movieDao.insert(trendingMovieOne)
        val trendingMovies = movieDao.getTrendingMovies()

        // Assert
        Truth.assertThat(trendingMovies.size).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].id).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].title).isEqualTo("trending movie 1")
        Truth.assertThat(trendingMovies[0].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_insert_inserts_single_upcoming_movie_row_into_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        // Act
        movieDao.insert(upcomingMovieOne)
        val upcomingMovies = movieDao.getUpcomingMovies()

        // Assert
        Truth.assertThat(upcomingMovies.size).isEqualTo(1)
        Truth.assertThat(upcomingMovies[0].id).isEqualTo(1)
        Truth.assertThat(upcomingMovies[0].title).isEqualTo("upcoming movie 1")
        Truth.assertThat(upcomingMovies[0].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_insert_inserts_multiple_trending_movie_rows_into_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        val trendingMovieTwo = MovieEntity.create(
            id = 2,
            title = "trending movie 2",
            category = Category.TRENDING
        )

        // Act
        movieDao.insert(listOf(trendingMovieOne, trendingMovieTwo))
        val trendingMovies = movieDao.getTrendingMovies()

        // Assert
        Truth.assertThat(trendingMovies.size).isEqualTo(2)
        Truth.assertThat(trendingMovies[0].id).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].title).isEqualTo("trending movie 1")
        Truth.assertThat(trendingMovies[0].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(trendingMovies[1].id).isEqualTo(2)
        Truth.assertThat(trendingMovies[1].title).isEqualTo("trending movie 2")
        Truth.assertThat(trendingMovies[1].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(movieDao.count()).isEqualTo(2)
    }

    @Test
    fun movieDao_insert_inserts_multiple_upcoming_movie_rows_into_movies_table() {
        // Arrange
        val upcomingMovie1 = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        val upcomingMovie2 = MovieEntity.create(
            id = 2,
            title = "upcoming movie 2",
            category = Category.UPCOMING
        )

        // Act
        movieDao.insert(listOf(upcomingMovie1, upcomingMovie2))
        val upcomingMovies = movieDao.getUpcomingMovies()

        // Assert
        Truth.assertThat(upcomingMovies.size).isEqualTo(2)
        Truth.assertThat(upcomingMovies[0].id).isEqualTo(1)
        Truth.assertThat(upcomingMovies[0].title).isEqualTo("upcoming movie 1")
        Truth.assertThat(upcomingMovies[0].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(upcomingMovies[1].id).isEqualTo(2)
        Truth.assertThat(upcomingMovies[1].title).isEqualTo("upcoming movie 2")
        Truth.assertThat(upcomingMovies[1].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(movieDao.count()).isEqualTo(2)
    }

    @Test
    fun movieDao_update_updates_single_trending_movie_row_in_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )
        movieDao.insert(trendingMovieOne)

        // Act
        val updatedTrendingMovieOne = trendingMovieOne.copy(title = "updated trending movie 1")
        movieDao.update(updatedTrendingMovieOne)
        val trendingMovies = movieDao.getTrendingMovies()

        // Assert
        Truth.assertThat(trendingMovies.size).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].id).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].title).isEqualTo(updatedTrendingMovieOne.title)
        Truth.assertThat(trendingMovies[0].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_update_updates_single_upcoming_movie_row_in_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )
        movieDao.insert(upcomingMovieOne)

        // Act
        val updatedUpcomingMovieOne = upcomingMovieOne.copy(title = "updated upcoming movie 1")
        movieDao.update(updatedUpcomingMovieOne)
        val upcomingMovies = movieDao.getUpcomingMovies()

        // Assert
        Truth.assertThat(upcomingMovies.size).isEqualTo(1)
        Truth.assertThat(upcomingMovies[0].id).isEqualTo(1)
        Truth.assertThat(upcomingMovies[0].title).isEqualTo(updatedUpcomingMovieOne.title)
        Truth.assertThat(upcomingMovies[0].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_delete_deletes_single_trending_movie_row_from_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )
        movieDao.insert(trendingMovieOne)

        // Act
        movieDao.delete(trendingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(0)
        Truth.assertThat(movieDao.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_delete_deletes_single_upcoming_movie_row_from_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )
        movieDao.insert(upcomingMovieOne)

        // Act
        movieDao.delete(upcomingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(0)
        Truth.assertThat(movieDao.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_delete_by_movieId_deletes_single_movie_row_from_movies_table(){
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )
        val trendingMovieOne = MovieEntity.create(
            id = 2,
            title = "trending movie 1",
            category = Category.TRENDING
        )
        movieDao.insert(listOf(upcomingMovieOne,trendingMovieOne))

        // Act
        movieDao.delete(upcomingMovieOne.id)

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(0)
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_delete_by_movieId_does_not_delete_any_rows_when_movieId_is_nonExistent(){
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )
        val trendingMovieOne = MovieEntity.create(
            id = 2,
            title = "trending movie 1",
            category = Category.TRENDING
        )
        movieDao.insert(listOf(upcomingMovieOne,trendingMovieOne))

        // Act
        movieDao.delete(NON_EXISTENT_MOVIE_ID)

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.count()).isEqualTo(2)
    }

    @Test
    fun moviesDao_delete_deletes_all_trending_movie_rows_from_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        val trendingMovieTwo = MovieEntity.create(
            id = 2,
            title = "trending movie 2",
            category = Category.TRENDING
        )

        movieDao.insert(listOf(trendingMovieOne, trendingMovieTwo))

        // Act
        movieDao.delete(listOf(trendingMovieOne,trendingMovieTwo))

        // Assert
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(0)
        Truth.assertThat(movieDao.count()).isEqualTo(0)
    }

    @Test
    fun moviesDao_delete_deletes_all_upcoming_movie_rows_from_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        val upcomingMovieTwo = MovieEntity.create(
            id = 2,
            title = "upcoming movie 2",
            category = Category.UPCOMING
        )
        movieDao.insert(listOf(upcomingMovieOne, upcomingMovieTwo))

        // Act
        movieDao.delete(listOf(upcomingMovieOne, upcomingMovieTwo))

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(0)
        Truth.assertThat(movieDao.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_upsert_updates_existing_trending_movie_row_in_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )
        movieDao.insert(trendingMovieOne)

        // Act
        val updatedTrendingMovieOne = trendingMovieOne.copy(title = "updated trending movie 1")
        movieDao.upsert(updatedTrendingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.getTrendingMovies()[0].id).isEqualTo(1)
        Truth.assertThat(movieDao.getTrendingMovies()[0].title).isEqualTo(updatedTrendingMovieOne.title)
        Truth.assertThat(movieDao.getTrendingMovies()[0].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_upsert_updates_existing_upcoming_movie_row_in_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )
        movieDao.insert(upcomingMovieOne)

        // Act
        val updatedUpcomingMovieOne = upcomingMovieOne.copy(title = "updated upcoming movie 1")
        movieDao.upsert(updatedUpcomingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.getUpcomingMovies()[0].id).isEqualTo(1)
        Truth.assertThat(movieDao.getUpcomingMovies()[0].title).isEqualTo(updatedUpcomingMovieOne.title)
        Truth.assertThat(movieDao.getUpcomingMovies()[0].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_upsert_inserts_non_existent_trending_movie_row_into_movies_table() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        // Act
        movieDao.upsert(trendingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getTrendingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.getTrendingMovies()[0].id).isEqualTo(1)
        Truth.assertThat(movieDao.getTrendingMovies()[0].title).isEqualTo("trending movie 1")
        Truth.assertThat(movieDao.getTrendingMovies()[0].category).isEqualTo(Category.TRENDING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_upsert_inserts_non_existent_upcoming_movie_row_into_movies_table() {
        // Arrange
        val upcomingMovieOne = MovieEntity.create(
            id = 1,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        // Act
        movieDao.upsert(upcomingMovieOne)

        // Assert
        Truth.assertThat(movieDao.getUpcomingMovies().size).isEqualTo(1)
        Truth.assertThat(movieDao.getUpcomingMovies()[0].id).isEqualTo(1)
        Truth.assertThat(movieDao.getUpcomingMovies()[0].title).isEqualTo("upcoming movie 1")
        Truth.assertThat(movieDao.getUpcomingMovies()[0].category).isEqualTo(Category.UPCOMING)
        Truth.assertThat(movieDao.count()).isEqualTo(1)
    }

    @Test
    fun movieDao_getTrendingMovies_returns_all_rows_where_category_equalsTo_trending(){
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        val trendingMovieTwo = MovieEntity.create(
            id = 2,
            title = "trending movie 2",
            category = Category.TRENDING
        )

        val upcomingMovieOne = MovieEntity.create(
            id = 3,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        movieDao.insert(listOf(trendingMovieOne, trendingMovieTwo, upcomingMovieOne))

        // Act
        val trendingMovies = movieDao.getTrendingMovies()

        // Assert
        Truth.assertThat(movieDao.count()).isEqualTo(3)

        Truth.assertThat(trendingMovies).isNotNull()
        Truth.assertThat(trendingMovies).isNotEmpty()
        Truth.assertThat(trendingMovies.size).isEqualTo(2)

        Truth.assertThat(trendingMovies[0].id).isEqualTo(1)
        Truth.assertThat(trendingMovies[0].title).isEqualTo("trending movie 1")
        Truth.assertThat(trendingMovies[0].category).isEqualTo(Category.TRENDING)

        Truth.assertThat(trendingMovies[1].id).isEqualTo(2)
        Truth.assertThat(trendingMovies[1].title).isEqualTo("trending movie 2")
        Truth.assertThat(trendingMovies[1].category).isEqualTo(Category.TRENDING)
    }

    @Test
    fun movieDao_getUpcomingMovies_returns_all_rows_where_category_equalsTo_upcoming() {
        // Arrange
        val trendingMovieOne = MovieEntity.create(
            id = 1,
            title = "trending movie 1",
            category = Category.TRENDING
        )

        val upcomingMovieOne = MovieEntity.create(
            id = 2,
            title = "upcoming movie 1",
            category = Category.UPCOMING
        )

        val upcomingMovieTwo = MovieEntity.create(
            id = 3,
            title = "upcoming movie 2",
            category = Category.UPCOMING
        )

        movieDao.insert(listOf(trendingMovieOne, upcomingMovieOne, upcomingMovieTwo))

        // Act
        val upcomingMovies = movieDao.getUpcomingMovies()

        // Assert
        Truth.assertThat(movieDao.count()).isEqualTo(3)

        Truth.assertThat(upcomingMovies).isNotNull()
        Truth.assertThat(upcomingMovies).isNotEmpty()
        Truth.assertThat(upcomingMovies.size).isEqualTo(2)

        Truth.assertThat(upcomingMovies[0].id).isEqualTo(2)
        Truth.assertThat(upcomingMovies[0].title).isEqualTo("upcoming movie 1")
        Truth.assertThat(upcomingMovies[0].category).isEqualTo(Category.UPCOMING)

        Truth.assertThat(upcomingMovies[1].id).isEqualTo(3)
        Truth.assertThat(upcomingMovies[1].title).isEqualTo("upcoming movie 2")
        Truth.assertThat(upcomingMovies[1].category).isEqualTo(Category.UPCOMING)
    }

    @Test
    fun movieDao_getTrendingMovies_returns_nonNull_emptyList_when_movies_table_is_empty() {
        // Arrange
        /**No more arrangement than [setUp] is needed here */

        // Act
        val trendingMovies = movieDao.getTrendingMovies()

        // Assert
        Truth.assertThat(trendingMovies).isEmpty()
        Truth.assertThat(trendingMovies).isNotNull()
        Truth.assertThat(trendingMovies.size).isEqualTo(0)
        Truth.assertThat(trendingMovies.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_getUpcomingMovies_returns_nonNull_emptyList_when_movies_table_is_empty() {
        // Arrange
        /**No more arrangement than [setUp] is needed here */

        // Act
        val upcomingMovies = movieDao.getUpcomingMovies()

        // Assert
        Truth.assertThat(upcomingMovies).isEmpty()
        Truth.assertThat(upcomingMovies).isNotNull()
        Truth.assertThat(upcomingMovies.size).isEqualTo(0)
        Truth.assertThat(upcomingMovies.count()).isEqualTo(0)
    }
}