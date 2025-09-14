package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.dao.SessionDao
import com.iutmetz.edt.data.local.entity.SessionEntity

class SessionRepositoryImpl(
    private val sessionDao: SessionDao, // on définit les objets nécessaires pour le repository
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
}