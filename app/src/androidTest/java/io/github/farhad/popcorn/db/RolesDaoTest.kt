package io.github.farhad.popcorn.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import io.github.farhad.popcorn.data.db.MovieDatabase
import io.github.farhad.popcorn.data.db.dao.MovieEntityDao
import io.github.farhad.popcorn.data.db.dao.RoleEntityDao
import io.github.farhad.popcorn.data.entity.Category
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import io.github.farhad.popcorn.fixture.create
import java.io.github.farhad.popcorn.fixture.create

@RunWith(AndroidJUnit4::class)
class RolesDaoTest {

    companion object {
        const val EXISTING_MOVIE_ID_ONE = 1
        const val EXISTING_MOVIE_ID_TWO = 2
        const val NON_EXISTENT_MOVIE_ID = 404
    }

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieEntityDao
    private lateinit var roleDao: RoleEntityDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        movieDao = movieDatabase.getMovieEntityDao()
        roleDao = movieDatabase.getRoleEntityDao()

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
    fun movieDatabase_has_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )

        // Act
        roleDao.insert(roleOne)

        // Assert
        Truth.assertThat(roleDao.count()).isEqualTo(1)
    }

    @Test
    fun roleDao_insert_inserts_single_role_row_into_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )

        // Act
        roleDao.insert(roleOne)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(1)
        Truth.assertThat(rolesList.first().id).isEqualTo("1")
        Truth.assertThat(rolesList.first().movieId).isEqualTo(PerformerDaoTest.EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList.first().job).isEqualTo("role one")
        Truth.assertThat(rolesList.first().name).isEqualTo("role taker one")
        Truth.assertThat(roleDao.count()).isEqualTo(1)
    }

    @Test
    fun roleDao_insert_inserts_multiple_role_rows_into_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )

        val roleTwo = RoleEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role two",
            name = "role taker two"
        )

        // Act
        roleDao.insert(listOf(roleOne, roleTwo))
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(2)
        Truth.assertThat(rolesList[0].id).isEqualTo("1")
        Truth.assertThat(rolesList[0].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList[0].job).isEqualTo("role one")
        Truth.assertThat(rolesList[0].name).isEqualTo("role taker one")
        Truth.assertThat(rolesList[1].id).isEqualTo("2")
        Truth.assertThat(rolesList[1].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList[1].job).isEqualTo("role two")
        Truth.assertThat(rolesList[1].name).isEqualTo("role taker two")
        Truth.assertThat(roleDao.count()).isEqualTo(2)
    }

    @Test
    fun roleDao_update_updates_single_role_row_in_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )
        roleDao.insert(roleOne)

        // Act
        val updatedRoleOne = roleOne.copy(name = "updated role taker one")
        roleDao.update(updatedRoleOne)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(1)
        Truth.assertThat(rolesList.first().id).isEqualTo("1")
        Truth.assertThat(rolesList.first().movieId).isEqualTo(PerformerDaoTest.EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList.first().job).isEqualTo("role one")
        Truth.assertThat(rolesList.first().name).isEqualTo("updated role taker one")
        Truth.assertThat(roleDao.count()).isEqualTo(1)
    }

    @Test
    fun roleDao_delete_deletes_single_role_row_from_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )
        roleDao.insert(roleOne)

        // Act
        roleDao.delete(roleOne)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(0)
        Truth.assertThat(roleDao.count()).isEqualTo(0)
    }

    @Test
    fun roleDao_delete_deletes_all_role_rows_from_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )

        val roleTwo = RoleEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role two",
            name = "role taker two"
        )
        roleDao.insert(listOf(roleOne, roleTwo))

        // Act
        roleDao.delete(listOf(roleOne, roleTwo))
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(0)
        Truth.assertThat(roleDao.count()).isEqualTo(0)
    }

    @Test
    fun roleDao_upsert_updates_existing_role_row_in_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )
        roleDao.insert(roleOne)

        // Act
        val updatedRoleOne = roleOne.copy(name = "updated role taker one")
        roleDao.upsert(updatedRoleOne)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(1)
        Truth.assertThat(rolesList.first().id).isEqualTo("1")
        Truth.assertThat(rolesList.first().movieId).isEqualTo(PerformerDaoTest.EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList.first().job).isEqualTo("role one")
        Truth.assertThat(rolesList.first().name).isEqualTo("updated role taker one")
        Truth.assertThat(roleDao.count()).isEqualTo(1)
    }

    @Test
    fun roleDao_upsert_inserts_non_existent_role_row_into_roles_table() {
        // Arrange
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,
            job = "role one",
            name = "role taker one"
        )

        // Act
        roleDao.upsert(roleOne)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList.size).isEqualTo(1)
        Truth.assertThat(rolesList.first().id).isEqualTo("1")
        Truth.assertThat(rolesList.first().movieId).isEqualTo(PerformerDaoTest.EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList.first().job).isEqualTo("role one")
        Truth.assertThat(rolesList.first().name).isEqualTo("role taker one")
        Truth.assertThat(roleDao.count()).isEqualTo(1)
    }

    @Test
    fun roleDao_getMovieRoleList_returns_all_role_rows_for_existing_movieId() {
        // Arrange
        /** part of arrangement has been done in [insertMovieRows] and [setUp]*/
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            job = "role one",
            name = "role taker one"
        )

        val roleTwo = RoleEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            job = "role two",
            name = "role taker two"
        )

        val roleThree = RoleEntity.create(
            id = "3",
            movieId = EXISTING_MOVIE_ID_TWO,// used when inserting trending movie
            job = "role two",
            name = "role taker two"
        )
        roleDao.insert(listOf(roleOne, roleTwo, roleThree))

        // Act
        val existingMovieRoleList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(roleDao.count()).isEqualTo(3)
        Truth.assertThat(existingMovieRoleList.size).isEqualTo(2)
        Truth.assertThat(existingMovieRoleList[0].id).isEqualTo("1")
        Truth.assertThat(existingMovieRoleList[0].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(existingMovieRoleList[0].job).isEqualTo("role one")
        Truth.assertThat(existingMovieRoleList[0].name).isEqualTo("role taker one")
        Truth.assertThat(existingMovieRoleList[1].id).isEqualTo("2")
        Truth.assertThat(existingMovieRoleList[1].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(existingMovieRoleList[1].job).isEqualTo("role two")
        Truth.assertThat(existingMovieRoleList[1].name).isEqualTo("role taker two")
    }

    @Test
    fun roleDao_getMovieRoleList_returns_nonNull_emptyList_for_nonExistent_movieId() {
        // Arrange
        /** part of arrangement has been done in [insertMovieRows] and [setUp]*/
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            job = "role one",
            name = "role taker one"
        )

        val roleTwo = RoleEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            job = "role two",
            name = "role taker two"
        )
        roleDao.insert(listOf(roleOne, roleTwo))

        // Act
        val nonExistingMovieRoles = roleDao.getMovieRolesList(NON_EXISTENT_MOVIE_ID)

        // Assert
        Truth.assertThat(nonExistingMovieRoles).isNotNull()
        Truth.assertThat(nonExistingMovieRoles.size).isEqualTo(0)
        Truth.assertThat(roleDao.count()).isEqualTo(2)

    }

    @Test
    fun roleDao_getMovieRoleList_returns_nonNull_emptyList_when_roles_table_is_empty() {
        // Arrange
        /** No more arrangement than [setUp] is needed here*/

        // Act
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(rolesList).isNotNull()
        Truth.assertThat(rolesList.size).isEqualTo(0)
        Truth.assertThat(roleDao.count()).isEqualTo(0)
    }

    @Test
    fun movieDao_delete_movie_row_also_deletes_relating_role_rows_in_roles_table() {
        // Arrange
        /** part of arrangement has been done in [insertMovieRows] and [setUp]*/
        val roleOne = RoleEntity.create(
            id = "1",
            movieId = EXISTING_MOVIE_ID_ONE,// used when inserting upcoming movie
            job = "role one",
            name = "role taker one"
        )

        val roleTwo = RoleEntity.create(
            id = "2",
            movieId = EXISTING_MOVIE_ID_TWO,// used when inserting trending movie
            job = "role two",
            name = "role taker two"
        )
        roleDao.insert(listOf(roleOne, roleTwo))

        // Act
        movieDao.delete(EXISTING_MOVIE_ID_TWO)
        val rolesList = roleDao.getMovieRolesList(EXISTING_MOVIE_ID_ONE)

        // Assert
        Truth.assertThat(movieDao.count()).isEqualTo(1)
        Truth.assertThat(rolesList).isNotNull()
        Truth.assertThat(rolesList.size).isEqualTo(1)
        Truth.assertThat(rolesList[0].id).isEqualTo("1")
        Truth.assertThat(rolesList[0].movieId).isEqualTo(EXISTING_MOVIE_ID_ONE)
        Truth.assertThat(rolesList[0].job).isEqualTo("role one")
        Truth.assertThat(rolesList[0].name).isEqualTo("role taker one")
    }
}