package io.github.farhad.popcorn.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.data.remote.api.ApiKeyInterceptor
import io.github.farhad.popcorn.data.remote.api.ApiService
import io.github.farhad.popcorn.data.remote.api.DateTypeConverter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideOkHttpClient(interceptors: Array<Interceptor>): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)

        interceptors.forEach { okHttpClient.addInterceptor(it) }

        return okHttpClient.build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        @NamedString(StringType.BASE_URL) baseUrl: String
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
    fun provideInterceptors(): Array<Interceptor> {
        return arrayOf(ApiKeyInterceptor())
    }

    @Provides
    @NamedString(StringType.API_KEY)
    fun provideApiKeyString(): String {
        return ""
    }

    @Provides
    @NamedString(StringType.BASE_URL)
    fun provideBaseUrl(): String {
        return ""
    }
}