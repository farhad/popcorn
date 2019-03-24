package io.github.farhad.popcorn.remote

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.google.gson.stream.MalformedJsonException
import io.github.farhad.popcorn.PopcornApp
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.data.remote.RemoteDataSource
import io.github.farhad.popcorn.fixture.RelayTransformer
import io.github.farhad.popcorn.testing.TestAppComponent
import io.github.farhad.popcorn.utils.loadFileFromAsset
import okhttp3.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import javax.inject.Inject

class ApiServiceTest {
    private val app: PopcornApp get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as PopcornApp

    @Inject
    lateinit var plugAndPlayInterceptor: PlugAndPlayInterceptor

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        (app.appComponent as TestAppComponent).inject(this)
    }

    @After
    fun tearDown() {
        plugAndPlayInterceptor.unPlug()
    }

    /**
     * creates an interceptor to return a mocked response, saved as json in assets folder, for a given url
     */
    private fun createMockResponseInterceptor(urlToMock: String, mockedResponsePath: String): Interceptor {

        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = chain.request().url().toString()
                val responseBuilder = Response.Builder().request(request)
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json")
                    .code(200)
                    .message("mocked response for $url")

                return if (url.endsWith(urlToMock)) {
                    responseBuilder.body(
                        ResponseBody.create(
                            MediaType.parse("application/json"),
                            loadFileFromAsset(app, mockedResponsePath)
                        )
                    ).build()
                } else {
                    chain.proceed(request)
                }
            }
        }
    }

    @Test
    fun apiKeyInterceptor_adds_apiKey_query_string_to_all_requests() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()
        val interceptor = RequestLoggerInterceptor()
        var queryParameter: String? = null

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)

        interceptor.request.subscribe {
            queryParameter = it.url().queryParameter("api_key")
        }

        remoteDataSource.getUpcomingMovies(1, transformer).test()

        // Assert
        Truth.assertThat(queryParameter).isNotNull()
        Truth.assertThat(queryParameter).isEqualTo("test-api-key")
    }

    @Test
    fun getUpcomingMovies_json_response_parses_to_movie_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()

        val interceptor = createMockResponseInterceptor(
            urlToMock = "movie/upcoming?page=1&api_key=test-api-key",
            mockedResponsePath = "json/upcomingOne.json"
        )

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)

        val subscription = remoteDataSource.getUpcomingMovies(1, transformer).test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 20 }
            .assertValue { movieList ->
                val item = movieList[0]
                return@assertValue item.id == 438650 && item.title == "Cold Pursuit"
            }
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getUpcomingMovies_throws_http_exception_when_status_code_is_not_200() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()

        val interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = chain.request().url().toString()
                return Response.Builder().request(request)
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json")
                    .code(401)
                    .message("mocked response for $url")
                    .body(
                        ResponseBody.create(
                            MediaType.parse("application/json"), "{}"
                        )
                    ).build()
            }
        }

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)
        val subscription = remoteDataSource.getUpcomingMovies(1, transformer).test()

        // Assert
        subscription.assertError { it is HttpException }
            .assertNotComplete()
    }

    @Test
    fun getUpcomingMovies_throws_malformedJsonException_when_malformed_response_is_received_with_status_code_200() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()
        val interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = chain.request().url().toString()
                return Response.Builder().request(request)
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json")
                    .code(200)
                    .message("mocked response for $url")
                    .body(
                        ResponseBody.create(
                            MediaType.parse("application/json"), "{ Malformed Json! }"
                        )
                    ).build()
            }
        }

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)
        val subscription = remoteDataSource.getUpcomingMovies(1, transformer).test()

        // Assert
        subscription.assertError { it is MalformedJsonException }
            .assertNotComplete()
    }

    @Test
    fun getTrendingMovies_json_response_parses_to_movie_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()

        val interceptor = createMockResponseInterceptor(
            urlToMock = "/trending/movie/week?page=1&api_key=test-api-key",
            mockedResponsePath = "json/trendingOne.json"
        )

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)

        val subscription = remoteDataSource.getTrendingMovies(1, transformer).test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 20 }
            .assertValue { movieList ->
                val item = movieList[0]
                return@assertValue item.id == 424694 && item.title == "Bohemian Rhapsody"
            }
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getMoviePerformers_of_WhileBoyRick_json_response_parses_to_performer_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<PerformerEntity>?>()

        val whiteRickBoyMovieId = 438808

        val interceptor = createMockResponseInterceptor(
            urlToMock = "movie/$whiteRickBoyMovieId/credits?api_key=test-api-key",
            mockedResponsePath = "json/movieCredit$whiteRickBoyMovieId.json"
        )

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)
        val subscription = remoteDataSource.getMoviePerformers(whiteRickBoyMovieId, transformer).test()

        // Assert
        subscription.assertValue { performerList -> performerList.isNotEmpty() }
            .assertValue { performerList -> performerList.size == 22 }
            .assertValue { performerList ->
                val item = performerList[0]
                return@assertValue item.id == "595e54719251410c56084dbd" && item.name == "Richie Merritt"
            }
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getMovieRoles_of_WhileBoyRick_json_response_parses_to_role_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<RoleEntity>?>()

        val whiteRickBoyMovieId = 438808

        val interceptor = createMockResponseInterceptor(
            urlToMock = "movie/$whiteRickBoyMovieId/credits?api_key=test-api-key",
            mockedResponsePath = "json/movieCredit$whiteRickBoyMovieId.json"
        )

        // Act
        plugAndPlayInterceptor.plugIn(interceptor)

        val subscription = remoteDataSource.getMovieRoles(whiteRickBoyMovieId, transformer).test()

        // Assert
        subscription.assertValue { roleList -> roleList.isNotEmpty() }
            .assertValue { roleList -> roleList.size == 26 }
            .assertValue { roleList ->
                val item = roleList[0]
                return@assertValue item.id == "58937069c3a3684a3c000639" && item.name == "Yann Demange"
            }
            .assertComplete()
            .assertNoErrors()
    }

}