package com.iutmetz.edt.ui.parametres

import android.content.res.Resources
import android.util.TypedValue
import androidx.lifecycle.ViewModel
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.repository.SessionRepository
import com.iutmetz.edt.util.SessionInit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel // cette annotation permet de rendre la classe injectable
class ParametresViewModel @Inject constructor( // cette classe permet de gérer la session qui abritera les paramètres de l'application
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private lateinit var _session: SessionEntity // la session utilisée par les paramètres
    val session: SessionEntity get() = _session
    private lateinit var _sessionOriginal: SessionEntity // la session original utilisée pour comparer et indiquer si des changements ont été effectués avant de quitter la page
    val sessionOriginal: SessionEntity get() = _sessionOriginal
    suspend fun chargeSession(theme: Resources.Theme): SessionEntity? { // cette fonction permet de charger la session de l'utilisateur si elle existe, sinon d'en créer une
        val session = sessionRepository.getSession() // on charge la session de l'utilisateur
        _session = session ?: SessionInit.withTheme(theme) // si la session existe on l'utilise sinon on en crée une
        _sessionOriginal =
            this.session.copy() // on copie la session pour comparer les changements plus tard
        return session // on retourne la session chargée au début
    }

    suspend fun saveSession() {
        sessionRepository.saveSession(session) // on sauvegarde la session de l'utilisateur
    }
}