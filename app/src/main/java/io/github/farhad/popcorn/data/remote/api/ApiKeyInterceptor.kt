package io.github.farhad.popcorn.data.remote.api

import io.github.farhad.popcorn.di.NamedString
import io.github.farhad.popcorn.di.StringType
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(@NamedString(StringType.API_KEY) val apiKey: String) : Interceptor {

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