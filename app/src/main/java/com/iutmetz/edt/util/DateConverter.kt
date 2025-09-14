package com.iutmetz.edt.util

import java.util.Date

object DateConverter {
    fun previousMonday(date: Date): Date {
        return Date(date.time).apply {
            val day = day // 0=dimanche, 1=lundi, ..., 6=samedi
            val diffToMonday = if (day == 0) -6 else 1 - day
            setDate(getDate() + diffToMonday)
            setHours(0)
            setMinutes(0)
            setSeconds(0)
        }
    }

    fun nextSunday(date: Date): Date {
        return Date(date.time).apply {
            val day = day // 0=dimanche, 1=lundi, ..., 6=samedi
            val diffToSunday = 7 - day
            setDate(getDate() + diffToSunday)
            setHours(0)
            setMinutes(0)
            setSeconds(0)
        }
    }

    fun fromRemote(date: String): Date {
        return Date(
            date.substring(0,4).toInt() - 1900,
            date.substring(4,6).toInt() - 1,
            date.substring(6,8).toInt(),
            date.substring(9,11).toInt(),
            date.substring(11,13).toInt(),
            date.substring(13,15).toInt()
        )
    }

    fun fromLocal(date: Date): String {
        return "${date.year + 1900}-${date.month + 1}-${date.date}"
    }

    fun weekToString(date: Date): String {
        val previousMonday = previousMonday(date)
        val nextSaturday = nextSunday(date).apply { this.date-- }
        return "${previousMonday.date}/${previousMonday.month + 1} - ${nextSaturday.date}/${nextSaturday.month + 1}"
    }
}