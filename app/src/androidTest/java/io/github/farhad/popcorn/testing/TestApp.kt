package io.github.farhad.popcorn.testing

import io.github.farhad.popcorn.PopcornApp
import io.github.farhad.popcorn.di.AppComponent

class TestApp : PopcornApp() {

    override var appComponent: AppComponent = DaggerTestAppComponent.builder().application(this).build()
}