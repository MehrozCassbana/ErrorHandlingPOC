package com.bykea.task.data.remote

import androidx.lifecycle.*
import com.bykea.task.core.networking.Resource
import javax.inject.Inject

/**
 * The database serves as the single source of truth.
 */

open class DataStoreHelper @Inject constructor() {

    suspend fun <T> fetchAndSaveInDb(databaseQuery: suspend () ->  T,
                                     networkCall: suspend () -> Resource<T>,
                                     saveCallResult: suspend (T) -> Unit,
                                     liveData: MutableLiveData<Resource<T>>) {
        liveData.postValue(Resource.Loading())
        liveData.postValue(Resource.Success(databaseQuery.invoke()))
        val responseStatus = networkCall.invoke()
        if (responseStatus is Resource.Success) {
            saveCallResult.invoke(responseStatus.data)
        }
        liveData.postValue(responseStatus)
    }

    suspend fun <T> fetchFromNetwork(networkCall: suspend () -> Resource<T>, liveData: MutableLiveData<Resource<T>>) {
        liveData.postValue(Resource.Loading())
        liveData.postValue(networkCall.invoke())
    }

}

