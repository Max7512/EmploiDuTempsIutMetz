package com.example.edt.data.repository

import com.example.edt.data.local.entity.AbbreviationEntity

interface AbbreviationRepository {
    suspend fun getAbbreviation(): List<AbbreviationEntity>
}