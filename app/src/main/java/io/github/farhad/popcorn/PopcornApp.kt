package io.github.farhad.popcorn

import android.app.Application
import io.github.farhad.popcorn.di.AppComponent
import io.github.farhad.popcorn.di.DaggerAppComponent
import io.github.farhad.popcorn.di.HasAppComponent

class PopcornApp : Application(), HasAppComponent {

    override fun onCreate() {
        super.onCreate()

    }

    override val appComponent: AppComponent
        get() = DaggerAppComponent.builder().application(this).build()
}