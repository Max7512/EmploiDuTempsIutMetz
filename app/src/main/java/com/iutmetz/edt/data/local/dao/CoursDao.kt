package com.iutmetz.edt.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.iutmetz.edt.data.local.entity.CoursEntity
import java.util.Date

@Dao
abstract class CoursDao() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(cours: CoursEntity)

    @Delete
    abstract suspend fun delete(cours: CoursEntity)

    @Transaction
    @Query("DELETE FROM cours WHERE debut BETWEEN :start AND :end")
    abstract suspend fun deleteRange(start: Date, end: Date)

    @Query("SELECT * FROM cours WHERE debut BETWEEN :start AND :end")
    abstract suspend fun getEdtRange(start: Date, end: Date): List<CoursEntity>
}