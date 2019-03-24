package io.github.farhad.popcorn

import android.app.Application
import io.github.farhad.popcorn.di.AppComponent
import io.github.farhad.popcorn.di.DaggerAppComponent
import io.github.farhad.popcorn.di.HasAppComponent

open class PopcornApp : Application(), HasAppComponent {

    override val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
}