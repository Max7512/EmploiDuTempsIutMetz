package com.iutmetz.edt.util

import android.content.res.Resources.Theme
import android.util.TypedValue
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.SessionEntity

object SessionInit {
    fun withTheme(theme: Theme): SessionEntity {
        val typedValue = TypedValue()

        theme.resolveAttribute(R.attr.coursColor, typedValue, true)
        val coursColor = typedValue.data

        theme.resolveAttribute(R.attr.coursTextColor, typedValue, true)
        val coursTextColor = typedValue.data

        theme.resolveAttribute(R.attr.bandeauColor, typedValue, true)
        val bandeauColor = typedValue.data

        theme.resolveAttribute(R.attr.bandeauTextColor, typedValue, true)
        val bandeauTextColor = typedValue.data

        theme.resolveAttribute(R.attr.saeColor, typedValue, true)
        val saeColor = typedValue.data

        theme.resolveAttribute(R.attr.saeTextColor, typedValue, true)
        val saeTextColor = typedValue.data

        theme.resolveAttribute(R.attr.todayBackgroundColor, typedValue, true)
        val todayBackgroundColor = typedValue.data

        return SessionEntity(
            coursColor = coursColor,
            coursTextColor = coursTextColor,
            bandeauColor = bandeauColor,
            bandeauTextColor = bandeauTextColor,
            saeColor = saeColor,
            saeTextColor = saeTextColor,
            todayBackgroundColor = todayBackgroundColor
        )
    }
}