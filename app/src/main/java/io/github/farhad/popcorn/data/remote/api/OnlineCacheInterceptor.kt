package io.github.farhad.popcorn.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * This interceptor prevents hitting the actual TMBD endpoints, if the request was made less than an hour ago.
 */
class OnlineCacheInterceptor : Interceptor {

    companion object {
        private const val maxAge = 60 * 60 * 1000 //online cache for 1 hour
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()

        return chain.proceed(request)
    }
}