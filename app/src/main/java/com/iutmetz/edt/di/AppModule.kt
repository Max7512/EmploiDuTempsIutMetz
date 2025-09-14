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
object AppModule {
    @Singleton
    @Provides
    fun provideVibration(@ApplicationContext context: Context): Vibrator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            @Suppress("DEPRECATION")
            return context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        } else {
            val vibratorManager =
                context.applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            return vibratorManager.defaultVibrator
        }
    }

    @Provides
    fun provideEdtRepository(
        apiService: ApiService,
        coursDao: CoursDao,
    ): EdtRepository =
        EdtRepositoryImpl(
            apiService,
            coursDao
        )

    @Provides
    fun provideAbbreviationRepository(
        apiService: ApiService,
        abbreviationDao: AbbreviationDao,
    ): AbbreviationRepository =
        AbbreviationRepositoryImpl(
            apiService,
            abbreviationDao
        )

    @Provides
    fun provideSessionRepository(
        sessionDao: SessionDao,
    ): SessionRepository =
        SessionRepositoryImpl(
            sessionDao
        )
}
