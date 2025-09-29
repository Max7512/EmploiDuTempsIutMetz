package com.iutmetz.edt.ui.parametres

import android.content.res.Resources
import android.util.TypedValue
import androidx.lifecycle.ViewModel
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.repository.SessionRepository
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
        if (session != null) { // si la session existe
            _session = session
        } else {
            val typedValue = TypedValue()

            theme.resolveAttribute(R.attr.coursColor, typedValue, true)
            val coursColor = typedValue.data

            theme.resolveAttribute(R.attr.coursTextColor, typedValue, true)
            val coursTextColor = typedValue.data

            theme.resolveAttribute(R.attr.bandeauColor, typedValue, true)
            val bandeauColor = typedValue.data

            theme.resolveAttribute(R.attr.bandeauTextColor, typedValue, true)
            val bandeauTextColor = typedValue.data

            _session = SessionEntity(coursColor = coursColor, coursTextColor = coursTextColor, bandeauColor = bandeauColor, bandeauTextColor = bandeauTextColor) // sinon on en crée une
        }
        _sessionOriginal = this.session.copy() // on copie la session pour comparer les changements plus tard
        return session // on retourne la session chargée au début
    }

    suspend fun saveSession() {
        sessionRepository.saveSession(session) // on sauvegarde la session de l'utilisateur
    }
}