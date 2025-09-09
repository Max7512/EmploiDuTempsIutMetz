package com.example.edt.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.edt.data.local.entity.AbbreviationEntity

@Dao
abstract class AbbreviationDao() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(abbreviations: AbbreviationEntity)

    @Delete
    abstract suspend fun delete(abbreviations: AbbreviationEntity)

    @Transaction
    @Query("DELETE FROM abbreviations WHERE 1=1")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM abbreviations")
    abstract suspend fun getAll(): List<AbbreviationEntity>
}