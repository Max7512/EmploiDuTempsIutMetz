package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.CoursEntity
import java.util.Date

interface EdtRepository { // on définit une interface qui permet de définir les fonctions attendues par un repository d'edt
    suspend fun getEdt(promo: String, date: Date): List<CoursEntity>
}