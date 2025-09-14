package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.dao.CoursDao
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.data.mapping.EdtMapper
import com.iutmetz.edt.data.remote.ApiService
import com.iutmetz.edt.util.DateConverter
import java.util.Date

class EdtRepositoryImpl(
    private val apiService: ApiService, // on définit les objets nécessaires pour le repository
    private val coursDao: CoursDao,
) : EdtRepository {
    override suspend fun getEdt(promo: String, date: Date): List<CoursEntity> { // cette fonction a pour but de récupérer l'emploi du temps depuis le serveur et les sauvegarder dans la base de données locale et les renvoyer ou de lire la base de données locale si il n'y a pas de connexion
        val dateStr = DateConverter.fromLocal(date) // la date demandée est convertie en chaine de caractère sous la forme yyyy-mm-dd pour la requête de l'emploi du temps
        val reponse = apiService.getEdtTxt(promo, dateStr) // on récupère la réponse du serveur

        val dateDeb = DateConverter.previousMonday(date) // on calcule la date de début et de fin de la semaine de la date demandée

        val dateFin = DateConverter.nextSunday(date)

        if (reponse.isSuccessful) { // si la réponse est valide on récupère l'emploi du temps et on l'insère dans la base de données locale
            val edt = EdtMapper.fromRemote(reponse.body()!!) // on convertit la réponse en une liste de CoursEntity

            coursDao.deleteRange(dateDeb, dateFin) // la base de données locale est vidée pour être mise à jour

            edt.forEach { cours ->
                coursDao.insert(cours) // l'emploi du temps est réinséré dans la base de données locale
            }

            return edt
        } else {
            return coursDao.getEdtRange(dateDeb, dateFin) // si la réponse n'est pas valide on renvoie l'emploi du temps de la base de données locale
        }
    }
}