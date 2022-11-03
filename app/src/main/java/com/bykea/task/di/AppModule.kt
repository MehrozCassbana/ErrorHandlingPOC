package com.bykea.task.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.bykea.task.utils.sharePrefs.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    /**
     * @param application
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideSharedPreference(application: Application): SharedPreferences {
        return application.getSharedPreferences(SharedPrefs.PREFS_NAME, Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideSharedPrefs(sharedPreferences: SharedPreferences, gson: Gson): SharedPrefs {
        return SharedPrefs(sharedPreferences, gson)
    }
    

}
