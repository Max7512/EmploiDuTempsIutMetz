package com.iutmetz.edt.data.local.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import java.util.Date

@Entity(
    tableName = "cours", // on définit la table de la base de données
    primaryKeys = ["id"], // on définit la clé primaire de la table
)
data class CoursEntity(
    val id: String,
    var titre: String,
    var salle: String,
    var prof: String,
    var groupe: String,
    var debut: Date,
    var fin: Date

    ) {

    override fun toString(): String {
        return "CoursEntity(id='$id', titre=$titre, salle='$salle', prof='$prof', groupe=$groupe, debut=$debut, fin=$fin)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoursEntity

        if (id != other.id) return false
        if (titre != other.titre) return false
        if (salle != other.salle) return false
        if (prof != other.prof) return false
        if (groupe != other.groupe) return false
        if (debut != other.debut) return false
        if (fin != other.fin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + titre.hashCode()
        result = 31 * result + salle.hashCode()
        result = 31 * result + prof.hashCode()
        result = 31 * result + groupe.hashCode()
        result = 31 * result + debut.hashCode()
        result = 31 * result + fin.hashCode()
        return result
    }

    fun copy(): CoursEntity = CoursEntity(
        id,
        titre,
        salle,
        prof,
        groupe,
        debut,
        fin
    )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CoursEntity>() {
            override fun areItemsTheSame(
                oldItem: CoursEntity,
                newItem: CoursEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CoursEntity,
                newItem: CoursEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}