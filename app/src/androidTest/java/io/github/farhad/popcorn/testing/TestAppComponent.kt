package io.github.farhad.popcorn.testing

import dagger.BindsInstance
import dagger.Component
import io.github.farhad.popcorn.PopcornApp
import io.github.farhad.popcorn.di.AppComponent
import io.github.farhad.popcorn.di.ApplicationScope
import io.github.farhad.popcorn.di.DataSourceModule
import io.github.farhad.popcorn.di.DbModule
import io.github.farhad.popcorn.remote.ApiServiceTest

@ApplicationScope
@Component(
    modules = [
        DbModule::class,
        TestNetworkingModule::class,
        DataSourceModule::class]
)
interface TestAppComponent : AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: PopcornApp): Builder

        fun build(): TestAppComponent
    }

    fun inject(test: ApiServiceTest)
}