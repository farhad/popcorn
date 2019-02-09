package io.github.farhad.popcorn.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.yossisegev.domain.common.RelayTransformer
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.domain.repository.MovieRepository
import io.github.farhad.popcorn.domain.usecase.GetMovieCast
import io.github.farhad.popcorn.domain.usecase.GetMovieCrew
import io.github.farhad.popcorn.domain.usecase.GetTrendingMovies
import io.github.farhad.popcorn.domain.usecase.GetUpcomingMovies
import io.github.farhad.popcorn.fixture.create
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DomainUsecasesTest {

    companion object {
        private const val NON_EXISTENT_MOVIE_ID = 404
    }

    private lateinit var repository: MovieRepository
    private lateinit var emptyRepository: MovieRepository

    private fun initMockedRepository() {
        /**
         * sample data for upcoming movies
         */
        val upcomingMovieOne = Movie.create(id = 1, title = "upcoming movie 1")
        val upcomingMovieTwo = Movie.create(id = 2, title = "upcoming movie 2")


        /**
         * sample data for trending movies
         */
        val trendingMovieOne = Movie.create(id = 3, title = "trending movie 1")
        val trendingMovieTwo = Movie.create(id = 4, title = "trending movie 2")

        /**
         * sample movie cast list
         */
        val performerOneOfUpcomingMovieOne = Performer.create(
            id = "5",
            movieId = upcomingMovieOne.id,
            name = "performer 1 of upcoming movie 1"
        )

        val performerTwoOfUpcomingMovieOne = Performer.create(
            id = "6",
            movieId = upcomingMovieOne.id,
            name = "performer 2 of upcoming movie 1"
        )

        /**
         * sample movie crew list
         */
        val roleOneOfTrendingMovieOne = Role.create(
            id = "7",
            movieId = trendingMovieOne.id,
            name = "role 1 of trending movie 1",
            job = "director"
        )

        repository = mock()
        Mockito.`when`(repository.getUpcomingMovies())
            .thenReturn(Observable.just(listOf(upcomingMovieOne, upcomingMovieTwo)))
        Mockito.`when`(repository.getTrendingMovies())
            .thenReturn(Observable.just(listOf(trendingMovieOne, trendingMovieTwo)))
        Mockito.`when`(repository.getMovieCast(upcomingMovieOne.id))
            .thenReturn(Observable.just(listOf(performerOneOfUpcomingMovieOne, performerTwoOfUpcomingMovieOne)))
        Mockito.`when`(repository.getMovieCrew(trendingMovieOne.id))
            .thenReturn(Observable.just(listOf(roleOneOfTrendingMovieOne)))
        Mockito.`when`(repository.getMovieCast(NON_EXISTENT_MOVIE_ID))
            .thenReturn(Observable.just(listOf()))
        Mockito.`when`(repository.getMovieCrew(NON_EXISTENT_MOVIE_ID))
            .thenReturn(Observable.just(listOf()))
    }

    private fun initEmptyMockedRepository() {
        emptyRepository = mock()
        Mockito.`when`(emptyRepository.getUpcomingMovies())
            .thenReturn(Observable.just(listOf()))
        Mockito.`when`(emptyRepository.getTrendingMovies())
            .thenReturn(Observable.just(listOf()))
        Mockito.`when`(emptyRepository.getMovieCast(any()))
            .thenReturn(Observable.just(listOf()))
        Mockito.`when`(emptyRepository.getMovieCrew(any()))
            .thenReturn(Observable.just(listOf()))
    }

    @Before
    fun setUp() {
        initMockedRepository()
        initEmptyMockedRepository()
    }

    @After
    fun tearDown(){
        Mockito.reset(repository)
        Mockito.reset(emptyRepository)
    }

    @Test
    fun getUpcomingMovies_usecase_returns_upcoming_movies() {
        // Arrange
        val transformer = RelayTransformer<List<Movie>>()
        val usecase = GetUpcomingMovies(transformer, repository)

        // Act
        val subscription = usecase.execute().test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 2 }
            .assertValue { movieList -> movieList[0].id == 1 && movieList[0].title == "upcoming movie 1" }
            .assertValue { movieList -> movieList[1].id == 2 && movieList[1].title == "upcoming movie 2" }
            .assertComplete()
    }

    @Test
    fun getUpcomingMovies_usecase_returns_emptyList_when_repository_is_empty() {
        // Arrange
        val transformer = RelayTransformer<List<Movie>>()
        val usecase = GetUpcomingMovies(transformer, emptyRepository)

        // Act
        val subscription = usecase.execute().test()

        // Assert
        subscription.assertValue { movieList -> movieList.isEmpty() }
            .assertComplete()
    }

    @Test
    fun getTrendingMovies_usecase_returns_trending_movies() {
        // Arrange
        val transformer = RelayTransformer<List<Movie>>()
        val usecase = GetTrendingMovies(transformer, repository)

        // Act
        val subscription = usecase.execute().test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 2 }
            .assertValue { movieList -> movieList[0].id == 3 && movieList[0].title == "trending movie 1" }
            .assertValue { movieList -> movieList[1].id == 4 && movieList[1].title == "trending movie 2" }
            .assertComplete()
    }

    @Test
    fun getTrendingMovies_usecase_returns_emptyList_when_repository_is_empty() {
        // Arrange
        val transformer = RelayTransformer<List<Movie>>()
        val usecase = GetTrendingMovies(transformer, emptyRepository)

        // Act
        val subscription = usecase.execute().test()

        // Assert
        subscription.assertValue { movieList -> movieList.isEmpty() }
            .assertComplete()
    }

    @Test
    fun getMovieCast_usecase_returns_performers_list_of_existing_movie() {
        // Arrange
        val transformer = RelayTransformer<List<Performer>>()
        val usecase = GetMovieCast(transformer, repository)

        // Act
        val params = GetMovieCast.Params.forMovie(1) //upcoming movie one
        val subscription = usecase.execute(params).test()

        // Assert
        subscription.assertValue { performerList -> performerList.isNotEmpty() }
            .assertValue { performerList -> performerList.size == 2 }
            .assertValue { performerList -> performerList[0].id == "5" && performerList[0].name == "performer 1 of upcoming movie 1" }
            .assertValue { performerList -> performerList[1].id == "6" && performerList[1].name == "performer 2 of upcoming movie 1" }
            .assertComplete()
    }

    @Test
    fun getMovieCast_usecase_returns_empty_performersList_for_nonExistent_movie() {
        // Arrange
        val transformer = RelayTransformer<List<Performer>>()
        val usecase = GetMovieCast(transformer, repository)

        // Act
        val params = GetMovieCast.Params.forMovie(NON_EXISTENT_MOVIE_ID)
        val subscription = usecase.execute(params).test()

        // Assert
        subscription.assertValue { performerList -> performerList.isEmpty() }
            .assertComplete()
    }

    @Test
    fun getMovieCast_usecase_throws_exception_when_movieId_is_null() {
        // Arrange
        val transformer = RelayTransformer<List<Performer>>()
        val usecase = GetMovieCast(transformer, repository)

        // Act
        val subscription = usecase.execute(null).test()

        // Assert
        subscription.assertError { it is IllegalArgumentException }
            .assertError { it.message == "movieId parameter cannot be null!" }
    }

    @Test
    fun getMovieCrew_usecase_returns_roles_list_of_existing_movie() {
        // Arrange
        val transformer = RelayTransformer<List<Role>>()
        val usecase = GetMovieCrew(transformer, repository)

        // Act
        val params = GetMovieCrew.Params.forMovie(3) //trending movie one
        val subscription = usecase.execute(params).test()

        // Assert
        subscription.assertValue { roleList -> roleList.isNotEmpty() }
            .assertValue { roleList -> roleList.size == 1 }
            .assertValue { roleList -> roleList[0].id == "7" && roleList[0].name == "role 1 of trending movie 1" }
            .assertComplete()
    }

    @Test
    fun getMovieCast_usecase_returns_empty_rolesList_for_nonExistent_movie() {
        // Arrange
        val transformer = RelayTransformer<List<Role>>()
        val usecase = GetMovieCrew(transformer, repository)

        // Act
        val params = GetMovieCrew.Params.forMovie(NON_EXISTENT_MOVIE_ID)
        val subscription = usecase.execute(params).test()

        // Assert
        subscription.assertValue { roleList -> roleList.isEmpty() }
            .assertComplete()
    }

    @Test
    fun getMovieCrew_usecase_throws_exception_when_movieId_is_null() {
        // Arrange
        val transformer = RelayTransformer<List<Role>>()
        val usecase = GetMovieCrew(transformer, repository)

        // Act
        val subscription = usecase.execute(null).test()

        // Assert
        subscription.assertError { it is IllegalArgumentException }
            .assertError { it.message == "movieId parameter cannot be null!" }
    }
}