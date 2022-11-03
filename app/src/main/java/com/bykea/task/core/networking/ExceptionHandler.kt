package com.bykea.task.core.networking

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandler @Inject constructor(private val gson: Gson) {
    fun parseError(throwable: Throwable?): Resource.ResourceError {
        return when (throwable) {
            is IOException -> Resource.ResourceError.NetworkError(ex = throwable)
            is HttpException -> {
                val errorResponse = convertErrorBody(throwable)
                Resource.ResourceError.GenericError(throwable.code(), errorResponse)
            }
            else -> {
                Resource.ResourceError.GenericError(0,
                        ErrorResponse(0, throwable?.message ?: "Some thing went wrong"))
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            val type = object : TypeToken<ErrorResponse>() {}.type
            gson.fromJson(throwable.response()?.errorBody()?.charStream(), type)
        } catch (exception: Exception) {
            null
        }
    }

    fun parseError(code: Int, errorBody: ResponseBody?): Resource.ResourceError {
        //todo check if we have any generic error model, Currently reading error node
        return errorBody?.let {
            val errorResponse = it.charStream().readText()
            if (errorResponse.isNotEmpty() && JSONObject(errorResponse).has("error")) {
                Resource.ResourceError.GenericError(code, ErrorResponse(code, JSONObject(errorResponse).getString("error")))
            } else {
                Resource.ResourceError.GenericError(code, ErrorResponse(code, "Some thing went wrong"))
            }
        } ?: run{
            Resource.ResourceError.GenericError(code, ErrorResponse(code, "Some thing went wrong"))
        }
    }
}