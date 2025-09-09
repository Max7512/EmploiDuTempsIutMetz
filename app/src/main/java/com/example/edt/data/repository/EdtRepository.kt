package com.example.edt.data.repository

import com.example.edt.data.local.entity.CoursEntity
import java.util.Date

interface EdtRepository {
    suspend fun getEdt(promo: String, date: Date): List<CoursEntity>
}