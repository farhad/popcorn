package io.github.farhad.popcorn.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.data.remote.api.*
import io.github.farhad.popcorn.fixture.RelayTransformer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    companion object {
        const val BASE_URL = "/"
        const val TEST_API_KEY = "test_api_key"
        const val WHITE_BOY_RICK_MOVIE_ID = 438808

        private fun loadFileFromAsset(path: String): String {
            val file = InstrumentationRegistry.getInstrumentation().context.assets.open(path)
            return file.bufferedReader().use { it.readText() }
        }
    }

    private lateinit var apiFactory: ApiFactory
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val httpUrl = mockWebServer.url(BASE_URL)

        val apiKeyInterceptor = ApiKeyInterceptor(TEST_API_KEY)
        val okHttpClient = MovieApiHttpClient.create(apiKeyInterceptor)
        val gsonFactory = GsonFactory()
        val retrofit = NetworkingConfig(httpUrl.toString(), okHttpClient, gsonFactory).retrofit()
        val apiService = retrofit.create(ApiService::class.java)
        apiFactory = ApiFactory(apiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun apiKeyInterceptor_adds_apiKey_query_string_to_all_requests() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // Act
        apiFactory.getUpcomingMovies(1, transformer).test()

        // Assert
        val request = mockWebServer.takeRequest().requestUrl
        Truth.assertThat(request.queryParameter("api_key")).isNotNull()
        Truth.assertThat(request.queryParameter("api_key")).isEqualTo(TEST_API_KEY)
    }

    @Test
    fun getUpcomingMovies_json_response_parses_to_movie_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()
        val mockedResponse = loadFileFromAsset("json/upcomingOne.json")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockedResponse))

        // Act
        val subscription = apiFactory.getUpcomingMovies(1, transformer).test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 20 }
            .assertValue { movieList ->
                val item = movieList[0]
                return@assertValue item.id == 438650 && item.title == "Cold Pursuit"
            }
            .assertComplete()
    }

    @Test
    fun getTrendingMovies_json_response_parses_to_movie_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<MovieEntity>?>()
        val mockedResponse = loadFileFromAsset("json/trendingOne.json")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockedResponse))

        // Act
        val subscription = apiFactory.getTrendingMovies(1, transformer).test()

        // Assert
        subscription.assertValue { movieList -> movieList.isNotEmpty() }
            .assertValue { movieList -> movieList.size == 20 }
            .assertValue { movieList ->
                val item = movieList[0]
                return@assertValue item.id == 424694 && item.title == "Bohemian Rhapsody"
            }
            .assertComplete()
    }

    @Test
    fun getMoviePerformers_of_WhileBoyRick_json_response_parses_to_performer_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<PerformerEntity>?>()
        val mockedResponse = loadFileFromAsset("json/movieCredit438808.json")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockedResponse))

        // Act
        val subscription = apiFactory.getMoviePerformers(WHITE_BOY_RICK_MOVIE_ID, transformer).test()

        // Assert
        subscription.assertValue { performerList -> performerList.isNotEmpty() }
            .assertValue { performerList -> performerList.size == 22 }
            .assertValue { performerList ->
                val item = performerList[0]
                return@assertValue item.id == "595e54719251410c56084dbd" && item.name == "Richie Merritt"
            }
            .assertComplete()
    }

    @Test
    fun getMovieRoles_of_WhileBoyRick_json_response_parses_to_role_entity_list_successfully() {
        // Arrange
        val transformer = RelayTransformer<List<RoleEntity>?>()
        val mockedResponse = loadFileFromAsset("json/movieCredit438808.json")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockedResponse))

        // Act
        val subscription = apiFactory.getMovieRoles(WHITE_BOY_RICK_MOVIE_ID, transformer).test()

        // Assert
        subscription.assertValue { roleList -> roleList.isNotEmpty() }
            .assertValue { roleList -> roleList.size == 26 }
            .assertValue { roleList ->
                val item = roleList[0]
                return@assertValue item.id == "58937069c3a3684a3c000639" && item.name == "Yann Demange"
            }
            .assertComplete()
    }

}