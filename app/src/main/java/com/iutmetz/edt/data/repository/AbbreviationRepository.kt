package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.AbbreviationEntity

interface AbbreviationRepository {
    suspend fun getAbbreviation(): List<AbbreviationEntity>
}