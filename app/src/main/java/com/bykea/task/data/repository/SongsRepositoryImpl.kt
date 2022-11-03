package com.bykea.task.data.repository

import androidx.lifecycle.LiveData
import com.bykea.task.core.SingleLiveEvent
import com.bykea.task.core.networking.CoreHelper
import com.bykea.task.core.networking.Resource
import com.bykea.task.data.remote.ApiService
import com.bykea.task.data.remote.DataStoreHelper
import com.bykea.task.data.remote.model.SearchResponse
import com.bykea.task.domain.SongsRepository

class SongsRepositoryImpl(
    private val apiService: ApiService,
    private val coreHelper: CoreHelper
) : SongsRepository, DataStoreHelper() {
    override suspend fun searchSongs(params: Map<String, String>) {
        fetchFromNetwork(
            networkCall = {
                coreHelper.serviceCall {
                    apiService.getSongs(params)
                }
            },
            liveData = _searchResponse
        )
    }

    private var _searchResponse = SingleLiveEvent<Resource<SearchResponse>>()
    override val searchResponse: LiveData<Resource<SearchResponse>>
        get() = _searchResponse
}