package com.iutmetz.edt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.iutmetz.edt.data.local.dao.AbbreviationDao
import com.iutmetz.edt.data.local.dao.CoursDao
import com.iutmetz.edt.data.local.dao.SessionDao
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.data.local.entity.SessionEntity
import java.util.Date

@Database( // l'annotation database permet de définir la base de données et ses paramètres
    entities = [
        CoursEntity::class,
        AbbreviationEntity::class, // les classes à utiliser sont définit
        SessionEntity::class
    ],
    autoMigrations = [],
    version = AppDatabase.DB_VERSION, // la version de la base de données, à chaque modification de la base de données, il faut incrémenter cette version ou supprimer les données de l'application sur le telephone
    exportSchema = true
)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() { // la base de données utilise le plugin room pour fonctionner
    abstract fun coursDao(): CoursDao
    abstract fun abbreviationDao(): AbbreviationDao // les différents DAO sont mis à disposition grâce à ces fonctions qui seront automatiquement générées par room
    abstract fun sessionDao(): SessionDao
    companion object {
        const val DB_VERSION: Int = 1 // la version de la base de données à changer à chaque modification de la base de données
        const val DB_NAME = "AppDatabase" // le nom de la base de données
    }

    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) } // la conversion du timestamp (nombre de milliseconde depuis le 1er Janvier 1970) en date pour récupérer un objet date
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? { // la conversion de la date en timestamp pour la base de données
            return date?.time
        }
    }
}