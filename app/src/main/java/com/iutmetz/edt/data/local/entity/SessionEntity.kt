package com.iutmetz.edt.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "session",
    primaryKeys = ["promo"]
)
data class SessionEntity(
    var promo: String,
    var groupe: String,

    ) {

    override fun toString(): String {
        return "SessionEntity(promo=$promo,groupe=$groupe,)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionEntity

        if (promo != other.promo) return false
        if (groupe != other.groupe) return false

        return true
    }

    override fun hashCode(): Int {
        var result = promo.hashCode()
        result = 31 * result + groupe.hashCode()
        return result
    }

    fun copy(): SessionEntity = SessionEntity(
        promo,
        groupe
    )
}