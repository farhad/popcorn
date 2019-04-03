package io.github.farhad.popcorn.data.remote.api

import android.content.Context
import io.github.farhad.popcorn.utils.isConnected
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * This interceptor takes care of using the offline cache for network responses in okhttp.
 * There are two scenarios where we use offline caching :
 * 1- calling apis defined in [ApiService]
 * 2- loading images with picasso.
 *
 * This is primarily created to help picasso load images when the device is offline
 * Although added to the oKHttpClient in retrofit, we bypass it with data from local database in practice.
 * see [AppRepository] for more details.
 */
class OfflineCacheInterceptor @Inject constructor(val context: Context) : Interceptor {

    companion object {
        private const val maxStale = 7 * 24 * 60 * 60 * 1000  //offline cache for 7 Days
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (isConnected(context) != true) {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }

        return chain.proceed(request)
    }
}