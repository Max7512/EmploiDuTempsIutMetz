package com.iutmetz.edt

import android.app.Application
import com.example.edt.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp // cette annotation permet de générer automatiquement le code pour l'injection de dépendance
class App : Application() { // cette classe est la classe principale de l'application et elle étend Application

    override fun onCreate() { // cette fonction est appelée au démarrage de l'application et initie le logging si l'application est en debug
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.Forest.plant(Timber.DebugTree())
        }
    }
}