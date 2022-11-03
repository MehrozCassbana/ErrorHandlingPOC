package com.bykea.task.core.networking


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading(val msg: String? = null) : Resource<Nothing>()
    sealed class ResourceError(val e: Exception?) : Resource<Nothing>() {
        data class GenericError(
            val code: Int? = null,
            val error: ErrorResponse? = null,
            val ex: Exception? = null
        ) : ResourceError(ex)

        data class NetworkError(val ex: Exception) : ResourceError(ex)
    }


}

data class ErrorResponse(val code: Int, val message: String?, val description: String = "")
