package io.github.farhad.popcorn.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.db.dao.MovieEntityDao
import io.github.farhad.popcorn.data.db.dao.PerformerEntityDao
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.fixture.create
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.github.farhad.popcorn.fixture.create

@RunWith(AndroidJUnit4::class)
class PerformerDaoTest {

    companion object {
        const val EXISTING_MOVIE_ID_ONE = 1
        const val EXISTING_MOVIE_ID_TWO = 2
        const val NON_EXISTENT_MOVIE_ID = 404
    }

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieEntityDao
    private lateinit var performerDao: PerformerEntityDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        movieDao = movieDatabase.getMovieEntityDao()
        performerDao = movieDatabase.getPerformerEntityDao()

        insertMovieRows()
    }

    private fun insertMovieRows() {
        val upcomingMovie = MovieEntity.create(
            id = EXISTING_MOVIE_ID_ONE,
            title = "upcoming movie",
            category = Category.UPCOMING
        )

        val trendingMovie = MovieEntity.create(
            id = EXISTING_MOVIE_ID_TWO,
            title = "trending movie",
            category = Category.TRENDING
        )
        movieDao.insert(listOf(upcomingMovie, trendingMovie))
    }

    @After
    fun tearDown() {
        movieDatabase.clearAllTables()
        movieDatabase.close()
    }

    @Test
    fun movieDatabase_has_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = 1,
            name = "performer one"
        )

        // Act
        performerDao.insert(performerOne)

        // Assert
        Truth.assertThat(performerDao.count()).isEqualTo(1)
    }

    @Test
    fun performerDao_insert_inserts_single_performer_row_into_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )

        // Act
        performerDao.insert(performerOne)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(1)
        Truth.assertThat(performerList.first().id).isEqualTo("1")
        Truth.assertThat(performerList.first().movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList.first().name).isEqualTo("performer one")
        Truth.assertThat(performerDao.count()).isEqualTo(1)
    }

    @Test
    fun performerDao_insert_inserts_multiple_performer_rows_into_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )

        val performerTwo = PerformerEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer two"
        )

        // Act
        performerDao.insert(listOf(performerOne, performerTwo))
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(2)
        Truth.assertThat(performerList[0].id).isEqualTo("1")
        Truth.assertThat(performerList[0].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList[0].name).isEqualTo("performer one")
        Truth.assertThat(performerList[1].id).isEqualTo("2")
        Truth.assertThat(performerList[1].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList[1].name).isEqualTo("performer two")
        Truth.assertThat(performerDao.count()).isEqualTo(2)
    }

    @Test
    fun performerDao_update_updates_single_performer_row_in_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )
        performerDao.insert(performerOne)

        // Act
        val updatedPerformerOne = performerOne.copy(name = "updated performer one")
        performerDao.update(updatedPerformerOne)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(1)
        Truth.assertThat(performerList.first().id).isEqualTo("1")
        Truth.assertThat(performerList.first().movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList.first().name).isEqualTo("updated performer one")
        Truth.assertThat(performerDao.count()).isEqualTo(1)
    }

    @Test
    fun performerDao_delete_deletes_single_performer_row_from_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )
        performerDao.insert(performerOne)

        // Act
        performerDao.delete(performerOne)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(0)
        Truth.assertThat(performerDao.count()).isEqualTo(0)
    }

    @Test
    fun performerDao_delete_deletes_all_performer_rows_from_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )

        val performerTwo = PerformerEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer two"
        )

        performerDao.insert(listOf(performerOne, performerTwo))

        // Act
        performerDao.delete(listOf(performerOne, performerTwo))
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(0)
        Truth.assertThat(performerDao.count()).isEqualTo(0)
    }

    @Test
    fun performerDao_upsert_updates_existing_performer_row_in_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )
        performerDao.insert(performerOne)

        // Act
        val updatedPerformerOne = performerOne.copy(name = "updated performer one")
        performerDao.upsert(updatedPerformerOne)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(1)
        Truth.assertThat(performerList.first().id).isEqualTo("1")
        Truth.assertThat(performerList.first().movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList.first().name).isEqualTo("updated performer one")
        Truth.assertThat(performerDao.count()).isEqualTo(1)
    }

    @Test
    fun performerDao_upsert_inserts_non_existent_performer_row_into_performers_table() {
        // Arrange
        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            name = "performer one"
        )

        // Act
        performerDao.upsert(performerOne)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerList.size).isEqualTo(1)
        Truth.assertThat(performerList.first().id).isEqualTo("1")
        Truth.assertThat(performerList.first().movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(performerList.first().name).isEqualTo("performer one")
        Truth.assertThat(performerDao.count()).isEqualTo(1)
    }

    @Test
    fun performerDao_getMoviePerformersList_returns_all_performer_rows_for_existing_movieId() {
        // Arrange
        /** part of arrangement has been done in [insertMovieRows] and [setUp]*/

        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            name = "performer one"
        )
        val performerTwo = PerformerEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            name = "performer two"
        )
        val performerThree = PerformerEntity.create(
            id = "3",
            movieId = EXISTING_MOVIE_ID_TWO,// used when inserting trending movie
            name = "performer three"
        )
        performerDao.insert(listOf(performerOne, performerTwo, performerThree))

        // Act
        val existingMoviePerformers = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerDao.count()).isEqualTo(3)
        Truth.assertThat(existingMoviePerformers.size).isEqualTo(2)
        Truth.assertThat(existingMoviePerformers[0].id).isEqualTo("1")
        Truth.assertThat(existingMoviePerformers[0].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(existingMoviePerformers[0].name).isEqualTo("performer one")
        Truth.assertThat(existingMoviePerformers[1].id).isEqualTo("2")
        Truth.assertThat(existingMoviePerformers[1].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(existingMoviePerformers[1].name).isEqualTo("performer two")
    }

    @Test
    fun performerDao_getMoviePerformerList_returns_nonNull_emptyList_for_nonExistent_movieId() {
        // Arrange
        /** part of arrangement has been done in [insertMovieRows] and [setUp]*/

        val performerOne = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_TWO,// used when inserting trending movie
            name = "performer one"
        )
        val performerTwo = PerformerEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_TWO,// used when inserting trending movie
            name = "performer two"
        )
        performerDao.insert(listOf(performerOne, performerTwo))

        // Act
        val nonExistingMoviePerformers = performerDao.getMoviePerformersList(NON_EXISTENT_MOVIE_ID)

        // Assert
        Truth.assertThat(nonExistingMoviePerformers).isNotNull()
        Truth.assertThat(nonExistingMoviePerformers.size).isEqualTo(0)
        Truth.assertThat(performerDao.count()).isEqualTo(2)
    }

    @Test
    fun performerDao_getMoviePerformerList_returns_nonNull_emptyList_when_performers_table_is_empty() {
        // Arrange
        /** No more arrangement than [setUp] is needed here*/

        // Act
        val performersList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performersList).isNotNull()
        Truth.assertThat(performersList.size).isEqualTo(0)
        Truth.assertThat(performerDao.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_delete_movie_row_also_deletes_relating_performer_rows_in_performers_table() {
        // Arrange
        /** part of arrangement has been done on [insertMovieRows] and [setUp]*/

        val performerOfUpcomingMovie = PerformerEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE, // used when inserting upcoming movie
            name = "performer of upcoming movie"
        )

        val performerOfTrendingMovie = PerformerEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_TWO, // used when inserting trending movie
            name = "performer of trending movie"
        )
        performerDao.insert(listOf(performerOfTrendingMovie, performerOfUpcomingMovie))

        // Act
        movieDao.delete(EXISTING_MOVIE_ID_TWO)
        val performerList = performerDao.getMoviePerformersList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(performerDao.count()).isEqualTo(1)
        Truth.assertThat(performerList).isNotNull()
        Truth.assertThat(performerList.size).isEqualTo(1)
        Truth.assertThat(performerList.first().id).isEqualTo(performerOfUpcomingMovie.id)
        Truth.assertThat(performerList.first().name).isEqualTo(performerOfUpcomingMovie.name)
        Truth.assertThat(performerList.first().movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
    }
}