package com.iutmetz.edt.ui.parametres

import androidx.lifecycle.ViewModel
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel // cette annotation permet de rendre la classe injectable
class ParametresViewModel @Inject constructor( // cette classe permet de gérer les données de la page d'emploi du temps
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private lateinit var _session: SessionEntity // idem que pour la liste des cours
    val session: SessionEntity get() = _session
    private lateinit var _sessionOriginal: SessionEntity
    val sessionOriginal: SessionEntity get() = _sessionOriginal
    suspend fun chargeSession(): SessionEntity? { // cette fonction permet de charger la session de l'utilisateur si elle existe, sinon d'en créer une
        val session = sessionRepository.getSession() // on charge la session de l'utilisateur
        _session = session ?: SessionEntity() // sinon on en crée une
        _sessionOriginal = this.session.copy()
        return session // on retourne la session chargée au début
    }

    suspend fun saveSession() {
        sessionRepository.saveSession(session) // on sauvegarde la session de l'utilisateur
    }
}