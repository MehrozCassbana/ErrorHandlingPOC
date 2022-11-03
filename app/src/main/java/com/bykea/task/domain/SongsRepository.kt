package com.bykea.task.domain

import androidx.lifecycle.LiveData
import com.bykea.task.core.networking.Resource
import com.bykea.task.data.remote.model.SearchResponse

interface SongsRepository {
    suspend fun searchSongs(params: Map<String, String>)
    val searchResponse: LiveData<Resource<SearchResponse>>
}