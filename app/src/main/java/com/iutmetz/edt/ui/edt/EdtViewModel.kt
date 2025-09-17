package com.iutmetz.edt.ui.edt

import androidx.lifecycle.ViewModel
import com.iutmetz.edt.BuildConfig
import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.data.repository.AbbreviationRepository
import com.iutmetz.edt.data.repository.EdtRepository
import com.iutmetz.edt.data.repository.SessionRepository
import com.iutmetz.edt.ui.edt.affichage.Affichage
import com.iutmetz.edt.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Retrofit
import java.util.Date
import javax.inject.Inject

@HiltViewModel // cette annotation permet de rendre la classe injectable
class EdtViewModel @Inject constructor( // cette classe permet de gérer les données de la page d'emploi du temps
    private val edtRepository: EdtRepository, // on injecte les repositories nécessaires pour la gestion des données
    private val abbreviationRepository: AbbreviationRepository,
    private val sessionRepository: SessionRepository,
    private val retrofit: Retrofit
) : ViewModel() {
    var promo: String = "" // on définit les variables nécessaires pour la gestion des données de l'emploi du temps

    var groupe: String = ""

    var groupes: MutableList<String> = mutableListOf()

    private var _date: Date = DateConverter.previousMonday(Date()) // on initialise la date au dernier Lundi, le début de la semaine actuelle
    var date: Date
        get() = _date // on définit les getters et setters pour la date de façon à ce qu'elle ne puisse que être au Lundi de la date passée en paramètre
        set(value) {
            _date = DateConverter.previousMonday(value)
        }

    private lateinit var _edt: List<CoursEntity> // la liste des cours est initialisée de façon à ne pas être modifiable en dehors de la classe
    val edt: List<CoursEntity> get() = _edt

    private lateinit var _abbreviations: List<AbbreviationEntity> // idem que pour la liste des cours
    val abbreviations: List<AbbreviationEntity> get() = _abbreviations

    private lateinit var _session: SessionEntity // idem que pour la liste des cours
    val session: SessionEntity get() = _session
    suspend fun refresh(affichage: Affichage): Result<List<CoursEntity>> { // cette fonction permet de rafraichir les données de l'emploi du temps et est une fonction suspend, voir EdtFragment.kt L124 pour plus d'informations
        val resultAbbreviation = abbreviationRepository.getAbbreviation(retrofit) // on charge les abbreviations et les cours
        val resultEdt = edtRepository.getEdt(promo, date, retrofit)

        _abbreviations = resultAbbreviation.data!! // on récupère les abbreviations et les cours
        _edt = resultEdt.data!!

        groupes.clear() // on vide la liste des groupes
        edt.map { it.groupe }.distinct().sorted().let { liste ->
            groupes.addAll(liste.filter { groupe ->
                !groupe.isEmpty() && liste.find {
                    it.length >= groupe.length && it.substring( // on filtre les groupes à partir pour ne garder que les plus précis soir le groupes de TP si possibles sinon les groupes normaux seront gardés,
                        0, // par exemple si des cours forment une liste de groupe de ["", "2", "2.1", "3", "1.1"], seuls les groupes 2.1, 3 et 1.1 seront gardés
                        groupe.length
                    ) == groupe && it != groupe
                } == null
            })
        }

        if (!groupes.isEmpty() && groupe.isEmpty()) groupe = groupes.first() // on met le premier groupe par défaut si aucun groupe n'est spécifié, c'est pour définir un groupe par défaut quand il y n'y a pas de session spécifiant un groupe

        session.promo = promo
        session.groupe = groupe // on met à jour la session avec les nouvelles données
        saveSession()

        affichage.afficher(edt, abbreviations, groupe) // l'affichage reçoit les données et les affiche selon sa méthode d'affichage

        return resultEdt
    }

    suspend fun chargeSession(): SessionEntity? { // cette fonction permet de charger la session de l'utilisateur si elle existe, sinon d'en créer une
        val session = sessionRepository.getSession() // on charge la session de l'utilisateur
        if (session != null) { // si la session existe
            _session = session
            promo = session.promo // on met à jour les variables de la classe
            groupe = session.groupe
        } else {
            _session = SessionEntity(promo, groupe) // sinon on en crée une
        }
        return session // on retourne la session chargée au début
    }

    suspend fun saveSession() {
        sessionRepository.saveSession(session) // on sauvegarde la session de l'utilisateur
    }

    fun nextWeek() {
        date = date.apply { date += 7 } // on ajoute 7 jours à la date
    }

    fun previousWeek() {
        date = date.apply { date -= 7 } // on enlève 7 jours à la date
    }

    suspend fun estAJour(): Boolean {
        val version = sessionRepository.checkVersion()
        version?.let {
            return BuildConfig.VERSION_NAME >= it.replace("v", "")
        }
        return true
    }
}