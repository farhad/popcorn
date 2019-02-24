package io.github.farhad.popcorn.data.remote.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class NetworkingConfig(
    private val baseUrl: String,
    private val httpClient: OkHttpClient,
    private val gsonFactory: GsonFactory
) {
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonFactory.create())
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()
    }
}

class GsonFactory {
    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateTypeConverter())
            .serializeNulls()
            .create()
    }

    fun create(): GsonConverterFactory = GsonConverterFactory.create(gson)
}

class MovieApiHttpClient {
    companion object {
        fun create(vararg interceptors: Interceptor): OkHttpClient {

            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)

            interceptors.forEach { okHttpClient.addInterceptor(it) }

            return okHttpClient.build()
        }
    }
}

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val url = request.url()
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}
