package com.iutmetz.edt.data.mapping

import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.remote.model.Abbreviation
import java.util.UUID

object AbbreviationMapper: Mapper<AbbreviationEntity, Abbreviation> {
    override fun fromRemote(r: Abbreviation): AbbreviationEntity { // la conversion de l'objet Abbreviation (serveur) en AbbreviationEntity (local)
        return AbbreviationEntity(
            UUID.randomUUID().toString(),
            r.mod_lib,
            r.mod_code
        )
    }
}