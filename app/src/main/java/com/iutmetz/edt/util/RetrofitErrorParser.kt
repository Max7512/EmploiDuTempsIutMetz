package com.iutmetz.edt.util

import com.iutmetz.edt.data.common.Error
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

object RetrofitErrorParser {
    fun parseError(
        response: Response<*>,
        retrofit: Retrofit
    ): Error? {
        val converter = retrofit.responseBodyConverter<Error>(
            Error::class.java,
            arrayOfNulls(0)
        )
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error.UNKNOWN_ERROR
        }
    }
}