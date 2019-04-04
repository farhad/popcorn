package io.github.farhad.popcorn.testing

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.farhad.popcorn.PopcornApp
import io.github.farhad.popcorn.di.*
import io.github.farhad.popcorn.remote.ApiServiceTest

@ApplicationScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        AppModule::class,
        ViewModelModule::class,
        DbModule::class,
        TestNetworkingModule::class,
        ImageModule::class,
        DataSourceModule::class,
        UsecaseModule::class]
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