package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.dao.SessionDao
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.remote.ApiService
import com.iutmetz.edt.data.remote.NetworkResponse
import retrofit2.Retrofit

class SessionRepositoryImpl(
    private val sessionDao: SessionDao, // on définit les objets nécessaires pour le repository
    private val apiService: ApiService,
    private val retrofit: Retrofit
) : SessionRepository {
    override suspend fun getSession(): SessionEntity? { // cette fonction renvoie la session sauvegardée dans la base de données locale ou null si aucune session n'est sauvegardée
        val session = sessionDao.getSession() // une liste de session est renvoyée par la base de données locale
        return if (session.isNotEmpty()) { session.first() } else { null } // si la liste n'est pas vide on renvoie la première session sinon on renvoie null
    }

    override suspend fun saveSession(session: SessionEntity) { // cette fonction sauvegarde la session dans la base de données locale
        val savedSession = getSession() // on récupère la session sauvegardée dans la base de données locale
        if (savedSession == null) {
            sessionDao.insert(session) // si aucune session n'est sauvegardée on l'insère dans la base de données locale
        } else {
            sessionDao.update(session.promo, session.groupe) // sinon on met à jour la session avec les nouvelles valeurs
        }
    }

    override suspend fun checkVersion(): String? { // cette fonction retourne la dernière version disponible sur le projet github ou null si une erreur se produit
        val githubBaseUrl = "https://api.github.com/repos/Max7512/EmploiDuTempsIutMetz" // on définit l'url du projet github

        val result = NetworkResponse.getResponse(retrofit, { apiService.getTags("$githubBaseUrl/tags") }, "") // on récupère les tags du projet github

        return result.data?.maxOfOrNull { it.name } // on retourne la dernière version disponible ou null s'il n'y a pas de données
    }
}