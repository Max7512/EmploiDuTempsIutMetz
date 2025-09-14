package com.iutmetz.edt.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import com.iutmetz.edt.data.local.dao.AbbreviationDao
import com.iutmetz.edt.data.local.dao.CoursDao
import com.iutmetz.edt.data.local.dao.SessionDao
import com.iutmetz.edt.data.remote.ApiService
import com.iutmetz.edt.data.repository.AbbreviationRepository
import com.iutmetz.edt.data.repository.AbbreviationRepositoryImpl
import com.iutmetz.edt.data.repository.EdtRepository
import com.iutmetz.edt.data.repository.EdtRepositoryImpl
import com.iutmetz.edt.data.repository.SessionRepository
import com.iutmetz.edt.data.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule { // ce module permet de définir les différents objets nécessaires pour l'application et de les injecter dans les classes qui les utilisent
    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideVibration(@ApplicationContext context: Context): Vibrator { // cette fonction permet de récupérer le service Vibrator de l'appareil, il est utilisé dans le BaseFragment pour la fonction vibrate
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            @Suppress("DEPRECATION")
            return context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        } else {
            val vibratorManager =
                context.applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            return vibratorManager.defaultVibrator
        }
    }

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideEdtRepository( // cette fonction permet de créer un objet EdtRepositoryImpl qui implémentera l'interface EdtRepository
        apiService: ApiService, // on définit les objets nécessaires pour le repository
        coursDao: CoursDao,
    ): EdtRepository =
        EdtRepositoryImpl( // on retourne un objet EdtRepositoryImpl avec les objets nécessaires
            apiService,
            coursDao
        )

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideAbbreviationRepository( // cette fonction permet de créer un objet AbbreviationRepositoryImpl qui implémentera l'interface AbbreviationRepository
        apiService: ApiService, // on définit les objets nécessaires pour le repository
        abbreviationDao: AbbreviationDao,
    ): AbbreviationRepository =
        AbbreviationRepositoryImpl( // on retourne un objet AbbreviationRepositoryImpl avec les objets nécessaires
            apiService,
            abbreviationDao
        )

    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideSessionRepository( // cette fonction permet de créer un objet SessionRepositoryImpl qui implémentera l'interface SessionRepository
        sessionDao: SessionDao, // on définit les objets nécessaires pour le repository
    ): SessionRepository =
        SessionRepositoryImpl( // on retourne un objet SessionRepositoryImpl avec les objets nécessaires
            sessionDao
        )
}
