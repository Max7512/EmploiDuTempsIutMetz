package com.iutmetz.edt.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "session", // on définit la table de la base de données
    primaryKeys = ["promo"] // on définit la clé primaire de la table
)
data class SessionEntity( // cette classe correspond au modèle de données des sessions dans la base de données locale
    var promo: String = "",
    var groupe: String = "",
    var coursColor: Int,
    var coursTextColor: Int,
    var bandeauColor: Int,
    var bandeauTextColor: Int,

    ) {

    override fun toString(): String {
        return "SessionEntity(promo=$promo,groupe=$groupe, coursColor=$coursColor, coursTextColor=$coursTextColor, bandeauColor=$bandeauColor, bandeauTextColor=$bandeauTextColor)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionEntity

        if (promo != other.promo) return false
        if (groupe != other.groupe) return false
        if (coursColor != other.coursColor) return false
        if (coursTextColor != other.coursTextColor) return false
        if (bandeauColor != other.bandeauColor) return false
        if (bandeauTextColor != other.bandeauTextColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = promo.hashCode()
        result = 31 * result + groupe.hashCode()
        result = 31 * result + coursColor
        result = 31 * result + coursTextColor
        result = 31 * result + bandeauColor
        result = 31 * result + bandeauTextColor
        return result
    }

    fun copy(): SessionEntity = SessionEntity(
        promo,
        groupe,
        coursColor,
        coursTextColor,
        bandeauColor,
        bandeauTextColor
    )
}