package com.example.edt.data.repository

import com.example.edt.data.local.entity.SessionEntity

interface SessionRepository {
    suspend fun getSession(): SessionEntity?

    suspend fun saveSession(session: SessionEntity)
}