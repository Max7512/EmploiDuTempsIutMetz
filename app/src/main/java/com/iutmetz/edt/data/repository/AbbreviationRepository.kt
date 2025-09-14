package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.AbbreviationEntity

interface AbbreviationRepository { // on définit une interface qui permet de définir les fonctions attendues par un repository d'abbreviation
    suspend fun getAbbreviation(): List<AbbreviationEntity>
}