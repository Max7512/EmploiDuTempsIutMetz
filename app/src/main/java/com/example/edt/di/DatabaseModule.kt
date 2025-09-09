package com.example.edt.di

import android.content.Context
import androidx.room.Room
import com.example.edt.data.local.AppDatabase
import com.example.edt.data.local.dao.AbbreviationDao
import com.example.edt.data.local.dao.CoursDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCoursDao(appDatabase: AppDatabase): CoursDao = appDatabase.coursDao()

    @Provides
    fun provideAbbreviationDao(appDatabase: AppDatabase): AbbreviationDao = appDatabase.abbreviationDao()
}
