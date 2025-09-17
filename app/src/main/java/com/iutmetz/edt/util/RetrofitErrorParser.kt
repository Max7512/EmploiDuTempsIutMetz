package com.iutmetz.edt.util

import com.iutmetz.edt.data.common.Error
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

object RetrofitErrorParser { // cette classe permet de formater les erreurs de la réponse du serveur
    fun parseError(
        response: Response<*>,
        retrofit: Retrofit
    ): Error? {
        val converter = retrofit.responseBodyConverter<Error>( // on récupère le convertisseur de la réponse du serveur
            Error::class.java,
            arrayOfNulls(0)
        )
        return try {
            converter.convert(response.errorBody()!!) // on essaie de convertit l'erreur en objet Error
        } catch (e: IOException) {
            Error.UNKNOWN_ERROR // sinon on renvoie une erreur inconnue
        }
    }
}