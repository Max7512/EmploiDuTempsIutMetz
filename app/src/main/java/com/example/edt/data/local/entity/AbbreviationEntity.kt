package com.example.edt.data.local.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import java.util.Date

@Entity(
    tableName = "abbreviations",
    primaryKeys = ["id"],
)
data class AbbreviationEntity(
    val id: String,
    var mod_lib: String,
    var mod_code: String,

    ) {

    override fun toString(): String {
        return "AbbreviationEntity(id='$id', mod_lib=$mod_lib, mod_code='$mod_code')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbbreviationEntity

        if (id != other.id) return false
        if (mod_lib != other.mod_lib) return false
        if (mod_code != other.mod_code) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + mod_lib.hashCode()
        result = 31 * result + mod_code.hashCode()
        return result
    }

    fun copy(): AbbreviationEntity = AbbreviationEntity(
        id,
        mod_lib,
        mod_code
    )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AbbreviationEntity>() {
            override fun areItemsTheSame(
                oldItem: AbbreviationEntity,
                newItem: AbbreviationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AbbreviationEntity,
                newItem: AbbreviationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}