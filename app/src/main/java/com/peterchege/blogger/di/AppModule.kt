package com.peterchege.blogger.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.room.database.BloggerDatabase
import com.peterchege.blogger.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserApi():BloggerApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(BloggerApi::class.java)
    }
    @Provides
    @Singleton
    fun provideSharedPreference(app:Application):SharedPreferences{
        return app.getSharedPreferences("user",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideBloggerDatabase(app: Application): BloggerDatabase {
        return Room.databaseBuilder(
            app,
            BloggerDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
}