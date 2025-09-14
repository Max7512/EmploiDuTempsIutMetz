package com.iutmetz.edt.data.mapping

interface Mapper<Local, Remote> {
    fun fromRemote(r: Remote): Local
}