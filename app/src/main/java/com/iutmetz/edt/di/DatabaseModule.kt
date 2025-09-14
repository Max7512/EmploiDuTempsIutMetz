package com.iutmetz.edt.di

import android.content.Context
import androidx.room.Room
import com.iutmetz.edt.data.local.AppDatabase
import com.iutmetz.edt.data.local.dao.AbbreviationDao
import com.iutmetz.edt.data.local.dao.CoursDao
import com.iutmetz.edt.data.local.dao.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule { // ce module permet de définir les différents objets nécessaires pour la base de données et de les injecter dans les classes qui les utilisent

    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME) // on utilise la classe Room pour créer la base de données selon la classe AppDatabase définit dans le package data.local
            .fallbackToDestructiveMigration()
            .build()

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideCoursDao(appDatabase: AppDatabase): CoursDao = appDatabase.coursDao() // on retourne un objet CoursDao

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideAbbreviationDao(appDatabase: AppDatabase): AbbreviationDao = appDatabase.abbreviationDao() // on retourne un objet AbbreviationDao

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideSessionDao(appDatabase: AppDatabase): SessionDao = appDatabase.sessionDao() // on retourne un objet SessionDao
}
