package com.iutmetz.edt.data.common

import androidx.annotation.StringRes
import com.iutmetz.edt.R
import com.google.gson.annotations.SerializedName

data class Error( // cette classe sert à gérer les erreurs de l'API
    @SerializedName("errorCode") val statusCode: Int = 0,
    @SerializedName("description") val statusMessage: String? = null,
    @SerializedName("code") val code: Int = 0,
    @StringRes val errorMessage: Int? = null
) {

    companion object {
        val NO_INTERNET_CONNECTION: Error =
            Error(503, errorMessage = R.string.err_connection)
        val UNKNOWN_ERROR = Error(1000, null, 0)
        val NOT_FOUND = Error(3, null, 404)
    }
}