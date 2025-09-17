package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import retrofit2.Retrofit

interface AbbreviationRepository { // on définit une interface qui permet de définir les fonctions attendues par un repository d'abbreviation
    suspend fun getAbbreviation(retrofit: Retrofit): Result<List<AbbreviationEntity>>
}