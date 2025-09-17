package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.dao.CoursDao
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.data.mapping.EdtMapper
import com.iutmetz.edt.data.remote.ApiService
import com.iutmetz.edt.data.remote.NetworkResponse
import com.iutmetz.edt.util.DateConverter
import retrofit2.Retrofit
import java.util.Date

class EdtRepositoryImpl(
    private val apiService: ApiService, // on définit les objets nécessaires pour le repository
    private val coursDao: CoursDao
) : EdtRepository {
    override suspend fun getEdt(
        promo: String,
        date: Date,
        retrofit: Retrofit
    ): Result<List<CoursEntity>> { // cette fonction a pour but de récupérer l'emploi du temps depuis le serveur et le sauvegarder dans la base de données locale puis de le renvoyer ou de lire la base de données locale si il n'y a pas de connexion
        val dateStr =
            DateConverter.fromLocal(date) // la date demandée est convertie en chaine de caractère sous la forme yyyy-mm-dd pour la requête de l'emploi du temps

        val dateDeb =
            DateConverter.previousMonday(date) // on calcule la date de début et de fin de la semaine de la date demandée
        val dateFin = DateConverter.nextSunday(date)

        val result = NetworkResponse.getResponse(
            retrofit,
            { apiService.getEdtTxt(promo, dateStr) },
            "Erreur de chargement des abbréviations"
        ) // on récupère la réponse du serveur

        val edt =
            mutableListOf<CoursEntity>() // on initialise une liste des cours en format local

        if (result.status == Result.Status.SUCCESS) { // si la réponse est valide on récupère les abbréviations et on les insère dans la base de données locale
            coursDao.deleteRange(dateDeb, dateFin) // la base de données locale est vidée dans la plage de date pour être mise à jour

            edt.addAll(EdtMapper.fromRemote(result.data!!)) // on convertit les données du serveur en coursEntity et on les ajoute à la liste des cours

            edt.forEach {
                coursDao.insert(it) // les cours sont réinsérés dans la base de données locale
            }
        } else {
            edt.addAll(coursDao.getEdtRange(dateDeb, dateFin)) // sinon on récupère les cours de la base de données locale
        }
        return Result(result.status, edt, result.error, result.message) // on retourne la réponse du serveur formatée
    }
}