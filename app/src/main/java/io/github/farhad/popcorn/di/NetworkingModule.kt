package io.github.farhad.popcorn.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.data.remote.api.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

@Module
class NetworkingModule {

    @Provides
    @ApplicationScope
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideOkHttpClient(interceptors: Array<Interceptor>, cache: Cache): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .cache(cache)

        interceptors.forEach { okHttpClient.addInterceptor(it) }

        return okHttpClient.build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        @NamedString(StringType.API_BASE_URL) baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converterFactory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): Converter.Factory {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateTypeConverter())
            .serializeNulls()
            .create()

        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideInterceptors(context: Context, @NamedString(StringType.API_KEY) apiKey: String): Array<Interceptor> {
        return arrayOf(
            ApiKeyInterceptor(apiKey),
            OnlineCacheInterceptor(),
            OfflineCacheInterceptor(context)
        )
    }

    /**
     * offline network cache to use with picasso.
     */
    @Provides
    fun provideOkhttpCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir, "picasso-cache")
        return Cache(httpCacheDirectory, 50 * 1024 * 1024) //50 MB of Disk Cache
    }

    @Provides
    @NamedString(StringType.API_KEY)
    fun provideApiKeyString(resources: Resources): String = resources.getString(R.string.api_key)

    @Provides
    @NamedString(StringType.API_BASE_URL)
    fun provideBaseUrl(resources: Resources): String = resources.getString(R.string.api_base_url)
}