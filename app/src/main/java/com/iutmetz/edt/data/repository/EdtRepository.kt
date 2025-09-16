package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.entity.CoursEntity
import retrofit2.Retrofit
import java.util.Date

interface EdtRepository { // on définit une interface qui permet de définir les fonctions attendues par un repository d'edt
    suspend fun getEdt(promo: String, date: Date, retrofit: Retrofit): Result<List<CoursEntity>>
}