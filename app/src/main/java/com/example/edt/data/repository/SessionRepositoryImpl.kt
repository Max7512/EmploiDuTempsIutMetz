package com.example.edt.data.repository

import com.example.edt.data.local.dao.SessionDao
import com.example.edt.data.local.entity.SessionEntity

class SessionRepositoryImpl(
    private val sessionDao: SessionDao,
) : SessionRepository {
    override suspend fun getSession(): SessionEntity? {
        val session = sessionDao.getSession()
        return if (session.isNotEmpty()) { session.first() } else { null }
    }

    override suspend fun saveSession(session: SessionEntity) {
        val savedSession = getSession()
        if (savedSession == null) {
            sessionDao.insert(session)
        } else {
            sessionDao.update(session.promo, session.groupe)
        }
    }
}