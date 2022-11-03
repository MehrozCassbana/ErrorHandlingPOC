package com.bykea.task.core.networking

import retrofit2.Response
import javax.inject.Inject

class CoreHelper @Inject constructor(
        private val exceptionHandler: ExceptionHandler) {
    suspend fun <T : Any> serviceCall(apiCall: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                exceptionHandler.parseError(response.code(), response.errorBody())
            }

        } catch (throwable: Throwable) {
            exceptionHandler.parseError(throwable)
        }
    }
}