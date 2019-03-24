package io.github.farhad.popcorn.remote

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestLoggerInterceptor : Interceptor {

    private val _request: BehaviorSubject<Request> = BehaviorSubject.create()
    val request : Observable<Request> = _request.distinctUntilChanged()

    override fun intercept(chain: Interceptor.Chain): Response {
        _request.onNext(chain.request())
        return chain.proceed(chain.request())
    }
}