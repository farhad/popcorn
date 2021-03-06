package io.github.farhad.popcorn.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.farhad.popcorn.PopcornApp

@ApplicationScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        AppModule::class,
        ViewModelModule::class,
        DbModule::class,
        NetworkingModule::class,
        ImageModule::class,
        DataSourceModule::class,
        UsecaseModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: PopcornApp): Builder

        fun build(): AppComponent
    }

    fun inject(app: PopcornApp)
}