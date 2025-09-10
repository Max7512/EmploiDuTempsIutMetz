package com.example.edt.data.mapping

import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.util.DateConverter

object EdtMapper: Mapper<List<CoursEntity>, String> {
    override fun fromRemote(r: String): List<CoursEntity> {
        val edt = mutableListOf<CoursEntity>()
        val vevents = r.split("BEGIN:VEVENT").toMutableList()
        vevents.removeAt(0)
        vevents.forEach { block ->
            val dtStartString = Regex("DTSTART:(\\d+T\\d+)").find(block)?.groupValues[1]
            val dtEndString = Regex("DTEND:(\\d+T\\d+)").find(block)?.groupValues[1]
            val dtStart = dtStartString?.let { DateConverter.fromRemote(it) }
            val dtEnd = dtEndString?.let { DateConverter.fromRemote(it) }
            val summary = Regex("SUMMARY:(.+)").find(block)?.groupValues[1]
            val location = Regex("LOCATION:(.+)").find(block)?.groupValues[1]
            val description = Regex("DTEND:(\\d+T\\d+)", RegexOption.DOT_MATCHES_ALL).find(block)?.groupValues[1]
            val uid = Regex("UID:(.+)").find(block)?.groupValues[1]
            uid?.let {
                val continuer = edt.find { uid == it.id }?.let { doublon ->
                    if (doublon.salle == "Salle???") {
                        edt.remove(doublon)
                        return@let true
                    } else {
                        return@let false
                    }
                } ?: true
                if (continuer) {
                    dtStart?.let {
                        dtEnd?.let {
                            summary?.let {
                                var salle = location?.trim() ?: "Salle???"
                                val ile = "Ile du Saulcy_"
                                if (salle.startsWith(ile)) {
                                    salle = salle.substring(ile.length).trim()
                                }
                                var prof = description?.trim() ?: "Prof???"
                                //console.log("prof", prof)
                                // toutes les infos en vrac séparées par des "vrais" \n
                                val lignes = prof.split(Regex("\r?\\n")).toMutableList()
                                for (i in lignes.size - 1 downTo 0 step 1) {
                                    // et parfois aussi des 0a0d (non visibles)
                                    lignes[i] = lignes[i].trim().replace(Regex("[\\r\\n]\\s+"), "")
                                    // j'essaye de ne garder que l'enseignant
                                    if (lignes[i].length <= 3 || lignes[i].contains("TD ") || lignes[i].contains("TP ") ||
                                        lignes[i].contains("CM ") || lignes[i].contains("EI ") || lignes[i].contains("Modifi") ||
                                        lignes[i].contains("(M")) {
                                        lignes.removeAt(i)
                                    }
                                }
                                //console.log("toutes les lignes", lignes)
                                prof = ""
                                //console.log("lignes", lignes)
                                if (lignes.isNotEmpty()) {
                                    lignes.forEach { ligne ->
                                        val np = ligne.split(" ")
                                        //console.log("np", np)
                                        // je m'assure que dans le champ il ya au mois 2 mots d'au moins 2 lettres
                                        // (c'est comme ça que j'identifie un prof :-( !!!)
                                        if (np.size >= 2 && np[0].length >= 2 && np[1].length >= 2 && sansChiffre(
                                                np
                                            )) {
                                            prof = ligne.trim()
                                            prof = extraitNom(prof)
                                            return@forEach
                                        }
                                    }
                                }
                                edt.add(
                                    CoursEntity(
                                        uid,
                                        summary.trim(),
                                        salle,
                                        prof,
                                        dtStart,
                                        dtEnd
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        return edt
    }

    fun sansChiffre(chaines: List<String>): Boolean {
        chaines.forEach { chaine ->
            if (Regex("\\d").containsMatchIn(chaine)) return false
        }
        return true
    }

    fun extraitNom(prof: String): String {
        var retour = ""
        retour.forEach { c ->
            if (c.isUpperCase()) {
                retour += c
            }
        }
        retour += "."
        // Inverser Nom Prénom en Prénom Nom si format "Nom P."
        //val match = retour.match(/^(.*)\s+([A-Z]\.)$/)
        val match = Regex("^(.*)\\s+([A-Z]\\.)$").find(retour)
        match?.let {
            val nom = it.groupValues[1]
            val initiale = it.groupValues[2]
            return "$initiale $nom"
        }
        return retour
    }
}