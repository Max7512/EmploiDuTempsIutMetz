package com.example.edt.data.repository

import com.example.edt.data.local.dao.AbbreviationDao
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.mapping.AbbreviationMapper
import com.example.edt.data.remote.ApiService

class AbbreviationRepositoryImpl(
    private val apiService: ApiService,
    private val abbreviationDao: AbbreviationDao,
) : AbbreviationRepository {
    override suspend fun getAbbreviation(): List<AbbreviationEntity> {
        val reponse = apiService.getAbbrevation()

        if (reponse.isSuccessful) {
            val abbreviations = mutableListOf<AbbreviationEntity>()
            reponse.body()!!.find {
                it.name == "abbreviations"
            }?.data?.forEach {
                abbreviations.add(AbbreviationMapper.fromRemote(it))
            }

            abbreviationDao.deleteAll()

            abbreviations.forEach { abbreviation ->
                abbreviationDao.insert(abbreviation)
            }

            return abbreviations
        } else {
            return abbreviationDao.getAll()
        }
    }
}