package com.example.edt.data.mapping

import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.data.remote.model.Abbreviation
import java.text.DateFormat
import java.util.UUID

object AbbreviationMapper: Mapper<AbbreviationEntity, Abbreviation> {
    override fun fromRemote(r: Abbreviation): AbbreviationEntity {
        return AbbreviationEntity(
            UUID.randomUUID().toString(),
            r.mod_lib,
            r.mod_code
        )
    }
}