package io.github.farhad.popcorn.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adding interceptors to OkHttpClient, after the OkhttpClientBuilder's build has been called is not possible and
 * will result in UnsupportedOperationException. The [PlugAndPlayInterceptor] helps test classes to plug in
 * interceptor to the OkhttpClient object of the app, prepared and injected from [TestNetworkingModule] and [NetworkingModule],
 * for manipulating or observing the networking functionality of the app.
 */

class PlugAndPlayInterceptor : Interceptor {

    private var interceptor: Interceptor? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        return if (interceptor != null)
            interceptor!!.intercept(chain)
        else
            chain.proceed(chain.request())
    }

    fun plugIn(interceptor: Interceptor) {
        this.interceptor = interceptor
    }

    fun unPlug() {
        this.interceptor = null
    }
}