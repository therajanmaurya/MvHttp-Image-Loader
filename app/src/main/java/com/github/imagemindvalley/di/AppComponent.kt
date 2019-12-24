package com.github.imagemindvalley.di

import android.app.Application
import com.github.imagemindvalley.ImageApplication
import com.github.mvhttpclient.di.MvHttpAppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MvHttpAppModule::class,
        AppModule::class,
        ActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(imageApplication: ImageApplication)
}
