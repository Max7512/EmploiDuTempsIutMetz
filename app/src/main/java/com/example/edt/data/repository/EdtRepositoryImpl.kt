package com.example.edt.data.repository

import com.example.edt.data.local.dao.CoursDao
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.data.mapping.EdtMapper
import com.example.edt.data.remote.ApiService
import com.example.edt.util.DateConverter
import java.util.Date

class EdtRepositoryImpl(
    private val apiService: ApiService,
    private val coursDao: CoursDao,
) : EdtRepository {
    override suspend fun getEdt(promo: String, date: Date): List<CoursEntity> {
        val dateStr = DateConverter.fromLocal(date)
        val reponse = apiService.getEdtTxt(promo, dateStr)

        val dateDeb = DateConverter.previousMonday(date)

        val dateFin = DateConverter.nextSunday(date)

        if (reponse.isSuccessful) {
            val edt = EdtMapper.fromRemote(reponse.body()!!)

            coursDao.deleteRange(dateDeb, dateFin)

            edt.forEach { cours ->
                coursDao.insert(cours)
            }

            return edt
        } else {
            return coursDao.getEdtRange(dateDeb, dateFin)
        }
    }
}