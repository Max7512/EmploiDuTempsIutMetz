package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.dao.AbbreviationDao
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.mapping.AbbreviationMapper
import com.iutmetz.edt.data.remote.ApiService
import com.iutmetz.edt.data.remote.NetworkResponse
import retrofit2.Retrofit

class AbbreviationRepositoryImpl(
    private val apiService: ApiService, // on définit les objets nécessaires pour le repository
    private val abbreviationDao: AbbreviationDao,
) : AbbreviationRepository {
    override suspend fun getAbbreviation(retrofit: Retrofit): Result<List<AbbreviationEntity>> { // cette fonction a pour but de récupérer les abbréviations depuis le serveur et les sauvegarder dans la base de données locale et les renvoyer ou de lire la base de données locale si il n'y a pas de connexion
        val result = NetworkResponse.getResponse(
            retrofit,
            { apiService.getAbbrevation() },
            "Erreur de chargement des abbréviations"
        ) // on récupère la réponse du serveur

        val abbreviations =
            mutableListOf<AbbreviationEntity>() // on initialise une liste d'abbréviations en format local

        if (result.status == Result.Status.SUCCESS) { // si la réponse est valide on récupère les abbréviations et on les insère dans la base de données locale

            result.data!!.find {
                it.name == "abbreviations"
            }?.data?.forEach { // si la réponse contient des données avec le nom abbréviations on parcourt les données associées et on les convertit en AbbreviationEntity et on les ajoute à la liste des abbréviations
                abbreviations.add(AbbreviationMapper.fromRemote(it))
            }

            abbreviationDao.deleteAll() // la base de données locale est vidée pour être mise à jour

            abbreviations.forEach { abbreviation ->
                abbreviationDao.insert(abbreviation) // les abbréviations sont réinsérées dans la base de données locale
            }
        } else {
            abbreviations.addAll(abbreviationDao.getAll()) // sinon on récupère les abbréviations de la base de données locale
        }
        return Result(result.status, abbreviations, result.error, result.message) // on retourne la réponse du serveur formatée
    }
}