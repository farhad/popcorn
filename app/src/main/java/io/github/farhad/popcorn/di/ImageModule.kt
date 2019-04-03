package io.github.farhad.popcorn.di

import android.content.Context
import android.content.res.Resources
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.github.farhad.popcorn.BuildConfig
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.utils.ImageLoader
import okhttp3.OkHttpClient


@Module
class ImageModule {

    @ApplicationScope
    @Provides
    fun providePicasso(
        context: Context,
        lruCache: LruCache,
        downloader: OkHttp3Downloader
    ): Picasso {

        return Picasso.Builder(context)
            .indicatorsEnabled(false) // show picasso loading india
            .memoryCache(lruCache)
            .loggingEnabled(BuildConfig.DEBUG)
            .downloader(downloader).build()
    }

    @ApplicationScope
    @Provides
    fun provideLruCache(): LruCache = LruCache(50 * 1024 * 1024)

    @ApplicationScope
    @Provides
    fun provideOkHttpDownloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @ApplicationScope
    @Provides
    fun provideImageLoader(picasso: Picasso): ImageLoader {
        return ImageLoader(picasso)
    }

    @Provides
    @NamedString(StringType.POSTER_BASE_URL)
    fun providePosterBaseUrl(resources: Resources): String =
        resources.getString(R.string.poster_base_url)

    @Provides
    @NamedString(StringType.BACKDROP_BASE_URL)
    fun provideBackdropBaseUrl(resources: Resources): String =
        resources.getString(R.string.backdrop_base_url)

    @Provides
    @NamedString(StringType.PERFORMER_ROLE_BASE_URL)
    fun providePerformersAndRolesBaseUrl(resources: Resources): String =
        resources.getString(R.string.performer_role_base_url)
}