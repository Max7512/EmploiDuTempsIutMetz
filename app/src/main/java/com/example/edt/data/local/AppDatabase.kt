package com.example.edt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.edt.data.local.dao.AbbreviationDao
import com.example.edt.data.local.dao.CoursDao
import com.example.edt.data.local.dao.SessionDao
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.data.local.entity.SessionEntity
import java.util.Date

@Database(
    entities = [
        CoursEntity::class,
        AbbreviationEntity::class,
        SessionEntity::class
    ],
    autoMigrations = [],
    version = AppDatabase.DB_VERSION,
    exportSchema = true
)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coursDao(): CoursDao
    abstract fun abbreviationDao(): AbbreviationDao
    abstract fun sessionDao(): SessionDao
    companion object {
        const val DB_VERSION: Int = 1
        const val DB_NAME = "AppDatabase"
    }

    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
    }
}