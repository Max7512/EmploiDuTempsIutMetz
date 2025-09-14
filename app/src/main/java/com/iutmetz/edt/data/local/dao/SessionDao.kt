package com.iutmetz.edt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iutmetz.edt.data.local.entity.SessionEntity

@Dao
abstract class SessionDao() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(session: SessionEntity)

    @Query("UPDATE session SET groupe = :groupe, promo = :promo WHERE 1 = 1")
    abstract suspend fun update(promo: String, groupe: String)

    @Query("SELECT * FROM session")
    abstract suspend fun getSession(): List<SessionEntity>
}