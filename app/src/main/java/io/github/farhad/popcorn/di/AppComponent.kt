package io.github.farhad.popcorn.di

import dagger.BindsInstance
import dagger.Component
import io.github.farhad.popcorn.PopcornApp
import io.github.farhad.popcorn.data.remote.api.ApiKeyInterceptor

@ApplicationScope
@Component(
    modules = [
        DbModule::class,
        NetworkingModule::class,
        DataSourceModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: PopcornApp): Builder

        fun build(): AppComponent
    }

    fun inject(apiKeyInterceptor: ApiKeyInterceptor)
}