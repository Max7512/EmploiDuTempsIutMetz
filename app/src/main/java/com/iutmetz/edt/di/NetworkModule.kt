package com.iutmetz.edt.di

import com.iutmetz.edt.BuildConfig
import com.iutmetz.edt.data.remote.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule { // ce module permet de définir les différents objets nécessaires pour les interactions avec le serveur et de les injecter dans les classes qui les utilisent
    var TRUST_ALL_CERTS: TrustManager = object : X509TrustManager { // cette classe permet de ne pas vérifier les certificats SSL à cause de certains appareils qui bloquent la communication vers le serveur
        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOf()
        }
    }
    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder() // on configure un objet Retrofit pour écrire des requêtes vers le serveur
        .baseUrl(BuildConfig.BASE_URL) // on définit l'url de base de l'API
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideGson(): Gson = GsonBuilder().create() // on configure un objet Gson pour décoder des objets JSON des réponses du serveur

    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) // on configure un objet HttpLoggingInterceptor pour afficher les logs de requêtes vers le serveur dans la console

    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideOkClient( // on configure un objet OkHttpClient pour lancer les requêtes vers le serveur
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, arrayOf(TRUST_ALL_CERTS), SecureRandom()) // on initialise le contexte SSL avec notre objet TRUST_ALL_CERTS qui permet de ne pas vérifier les certificats SSL

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .sslSocketFactory(sslContext.socketFactory, TRUST_ALL_CERTS as X509TrustManager) // on ajoute notre objet TRUST_ALL_CERTS à l'objet OkHttpClient
            .build()
    }

    @Singleton // cette annotation permet de définir un objet singleton qui sera partagé par toutes les classes qui l'utilisent
    @Provides // cette annotation permet de définir une fonction qui sera appelée par dagger pour créer un objet
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java) // on retourne un objet ApiService final grâce à Retrofit qui est configuré au dessus
}