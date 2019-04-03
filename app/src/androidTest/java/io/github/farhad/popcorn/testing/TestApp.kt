package io.github.farhad.popcorn.testing

import io.github.farhad.popcorn.PopcornApp

class TestApp : PopcornApp() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTestAppComponent.builder().application(this).build()
    }
}