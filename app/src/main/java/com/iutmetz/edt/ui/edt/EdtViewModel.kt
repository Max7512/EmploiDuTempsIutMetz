package com.iutmetz.edt.ui.edt

import androidx.lifecycle.ViewModel
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.repository.AbbreviationRepository
import com.iutmetz.edt.data.repository.EdtRepository
import com.iutmetz.edt.data.repository.SessionRepository
import com.iutmetz.edt.ui.edt.affichage.Affichage
import com.iutmetz.edt.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EdtViewModel @Inject constructor(
    private val edtRepository: EdtRepository,
    private val abbreviationRepository: AbbreviationRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    var promo: String = ""

    var groupe: String = ""

    var groupes: MutableList<String> = mutableListOf()

    private var _date: Date = DateConverter.previousMonday(Date())
    var date: Date
        get() = _date
        set(value) {
            _date = DateConverter.previousMonday(value)
        }

    private lateinit var _edt: List<CoursEntity>
    val edt: List<CoursEntity> get() = _edt

    private lateinit var _abbreviations: List<AbbreviationEntity>
    val abbreviations: List<AbbreviationEntity> get() = _abbreviations

    private lateinit var _session: SessionEntity
    val session: SessionEntity get() = _session
    suspend fun refresh(affichage: Affichage) {
        _abbreviations = abbreviationRepository.getAbbreviation()
        _edt = edtRepository.getEdt(promo, date)

        groupes.clear()
        edt.map { it.groupe }.distinct().sorted().let { liste ->
            groupes.addAll(liste.filter { groupe ->
                !groupe.isEmpty() && liste.find {
                    it.length >= groupe.length && it.substring(
                        0,
                        groupe.length
                    ) == groupe && it != groupe
                } == null
            })
        }

        if (!groupes.isEmpty() && groupe.isEmpty()) groupe = groupes.first()

        session.promo = promo
        session.groupe = groupe
        saveSession()

        affichage.afficher(edt, abbreviations, groupe)
    }

    suspend fun chargeSession(): SessionEntity? {
        val session = sessionRepository.getSession()
        if (session != null) {
            _session = session
            promo = session.promo
            groupe = session.groupe
        } else {
            _session = SessionEntity(promo, groupe)
        }
        return session
    }

    suspend fun saveSession() {
        sessionRepository.saveSession(session)
    }

    fun nextWeek() {
        date = date.apply { date += 7 }
    }

    fun previousWeek() {
        date = date.apply { date -= 7 }
    }
}