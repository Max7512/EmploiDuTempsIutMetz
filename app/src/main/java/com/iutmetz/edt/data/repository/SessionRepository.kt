package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.SessionEntity

interface SessionRepository { // on définit une interface qui permet de définir les fonctions attendues par un repository de session
    suspend fun getSession(): SessionEntity?

    suspend fun saveSession(session: SessionEntity)

    suspend fun checkVersion(): String?
}