package com.iutmetz.edt.data.remote

import com.iutmetz.edt.data.common.Error
import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.util.RetrofitErrorParser
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber

object NetworkResponse { // cette classe sert à faciliter la gestion des réponses du serveur et à gérer les erreurs
    @Suppress("CascadeIf")
    suspend fun <T> getResponse(
        retrofit: Retrofit,
        request: suspend () -> Response<T>,
        defaultMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                Result.success(result.body())
            } else {
                val errorResponse = RetrofitErrorParser.parseError(result, retrofit)

                if (result.code() == 503) {
                    Result.error<T>(Error.NO_INTERNET_CONNECTION)
                } else if (errorResponse != null)
                    Result.error(error = errorResponse, message = errorResponse.statusMessage)
                else
                    Result.error(Error(0, defaultMessage))
            }
        } catch (e: Throwable) {
            Timber.e(e)
            Result.error(Error(0, e.message))
        }
    }
}