package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.SessionEntity

interface SessionRepository {
    suspend fun getSession(): SessionEntity?

    suspend fun saveSession(session: SessionEntity)
}