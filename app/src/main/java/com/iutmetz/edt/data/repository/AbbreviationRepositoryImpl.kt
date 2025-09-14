package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.dao.AbbreviationDao
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.mapping.AbbreviationMapper
import com.iutmetz.edt.data.remote.ApiService

class AbbreviationRepositoryImpl(
    private val apiService: ApiService, // on définit les objets nécessaires pour le repository
    private val abbreviationDao: AbbreviationDao,
) : AbbreviationRepository {
    override suspend fun getAbbreviation(): List<AbbreviationEntity> { // cette fonction a pour but de récupérer les abbréviations depuis le serveur et les sauvegarder dans la base de données locale et les renvoyer ou de lire la base de données locale si il n'y a pas de connexion
        val reponse = apiService.getAbbrevation() // on récupère la réponse du serveur

        if (reponse.isSuccessful) { // si la réponse est valide on récupère les abbréviations et on les insère dans la base de données locale
            val abbreviations = mutableListOf<AbbreviationEntity>()
            reponse.body()!!.find {
                it.name == "abbreviations"
            }?.data?.forEach { // si la réponse contient des données avec le nom abbréviations on parcourt les données associées et on les convertit en AbbreviationEntity et on les ajoute à la liste des abbréviations
                abbreviations.add(AbbreviationMapper.fromRemote(it))
            }

            abbreviationDao.deleteAll() // la base de données locale est vidée pour être mise à jour

            abbreviations.forEach { abbreviation ->
                abbreviationDao.insert(abbreviation) // les abbréviations sont réinsérées dans la base de données locale
            }

            return abbreviations
        } else {
            return abbreviationDao.getAll() // si la réponse n'est pas valide on renvoie les abbréviations de la base de données locale
        }
    }
}