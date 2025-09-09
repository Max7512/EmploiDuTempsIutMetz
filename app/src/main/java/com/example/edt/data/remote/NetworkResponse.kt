package com.example.edt.data.remote

import com.example.edt.data.common.Result
import com.example.edt.data.common.Error
import retrofit2.Response
import timber.log.Timber

object NetworkResponse {
    @Suppress("CascadeIf")
    suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultMessage: String
    ): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                Timber.d("Success network request. Body: ${result.body()}")
                Result.success(result.body())
            } else {
                Timber.e("Error network request. Error: ${result.errorBody()}")

                if (result.code() == 503) {
                    Result.error<T>(Error.NO_INTERNET_CONNECTION)
                } else
                    Result.error(Error(0, defaultMessage))
            }
        } catch (e: Throwable) {
            Timber.e(e)
            Result.error(Error(0, e.message))
        }
    }
}