package com.iutmetz.edt.util

import java.util.Date

object DateConverter { // cette objet permet de manipuler et convertir des date
    fun previousMonday(date: Date): Date { // cette fonction permet de récupérer la date du lundi précédent
        return Date(date.time).apply { // on crée une date à partir de la date passée en paramètre
            val day = day // 0=dimanche, 1=lundi, ..., 6=samedi // on récupère le jour de la semaine de la date
            val diffToMonday = if (day == 0) -6 else 1 - day // on calcule la différence de jour pour aller au lundi précédent
            setDate(getDate() + diffToMonday) // on ajoute la différence de jour à la date
            setHours(0) // on remet l'heure à 0
            setMinutes(0) // on remet les minutes à 0
            setSeconds(0) // on remet les secondes à 0
        }
    }

    fun nextSunday(date: Date): Date { // cette fonction permet de récupérer la date du dimanche suivant
        return Date(date.time).apply { // on crée une date à partir de la date passée en paramètre
            val day = day // 0=dimanche, 1=lundi, ..., 6=samedi // on récupère le jour de la semaine de la date
            val diffToSunday = 7 - day // on calcule la différence de jour pour aller au dimanche suivant
            setDate(getDate() + diffToSunday) // on ajoute la différence de jour à la date
            setHours(0) // on remet l'heure à 0
            setMinutes(0) // on remet les minutes à 0
            setSeconds(0) // on remet les secondes à 0
        }
    }

    fun fromRemote(date: String): Date { // cette fonction permet de convertir une date de réponse de l'API en objet Date
        return Date( // on lit la date et on en crée un objet Date
            date.substring(0,4).toInt() - 1900,
            date.substring(4,6).toInt() - 1,
            date.substring(6,8).toInt(),
            date.substring(9,11).toInt(),
            date.substring(11,13).toInt(),
            date.substring(13,15).toInt()
        )
    }

    fun fromLocal(date: Date): String { // cette fonction permet de convertir un objet Date en date lisible par l'API
        return "${date.year + 1900}-${date.month + 1}-${date.date}" // on retourne la date au format YYYY-MM-DD
    }

    fun weekToString(date: Date): String { // cette fonction permet de convertir un objet Date en une chaine de caractère lisible par l'utilisateur
        val previousMonday = previousMonday(date) // on récupère la date du lundi précédent
        val nextSaturday = nextSunday(date).apply { this.date-- } // on récupère la date du dimanche suivant et on décrémente la date pour avoir la date du samedi
        return "${previousMonday.date}/${previousMonday.month + 1} - ${nextSaturday.date}/${nextSaturday.month + 1}" // on retourne la date au format DD/MM - DD/MM
    }
}