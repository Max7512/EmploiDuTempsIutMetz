package com.iutmetz.edt.data.mapping

interface Mapper<Local, Remote> { // l'interface Mapper permet de convertir des objets serveur en objets locaux
    fun fromRemote(r: Remote): Local
}