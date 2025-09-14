package com.iutmetz.edt.data.repository

import com.iutmetz.edt.data.local.entity.CoursEntity
import java.util.Date

interface EdtRepository {
    suspend fun getEdt(promo: String, date: Date): List<CoursEntity>
}