package com.github.mvhttpclient.di

import android.app.Application
import androidx.room.Room
import com.github.mvhttpclient.api.MvHttpService
import com.github.mvhttpclient.db.ImageDataDao
import com.github.mvhttpclient.db.MvHttpDb
import com.github.mvhttpclient.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MvHttpAppModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun profileOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }
    }

    @Singleton
    @Provides
    fun provideMvHttpService(okHttpClient: OkHttpClient.Builder): MvHttpService {
        return Retrofit.Builder()
            .baseUrl("https://pastebin.com/raw/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient.build())
            .build()
            .create(MvHttpService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): MvHttpDb {
        return Room
            .inMemoryDatabaseBuilder(app, MvHttpDb::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideImageDataDao(db: MvHttpDb): ImageDataDao {
        return db.imageDataDao()
    }
}
