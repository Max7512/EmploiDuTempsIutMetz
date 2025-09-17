package com.iutmetz.edt.data.common

data class Result<out T>( // cette classe sert à donner plus d'informations sur les réponses de l'API
    val status: Status,
    val data: T?,
    val error: Error? = null,
    val message: String? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
    }

    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null, null)
        }

        fun <T> error(error: Error? = null, message: String? = null, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, error, message)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }


}