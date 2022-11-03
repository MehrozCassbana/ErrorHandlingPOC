package com.bykea.task.data.remote

import com.bykea.task.data.remote.ApiEndPoint.FETCH_SONGS_URL
import com.bykea.task.data.remote.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET(FETCH_SONGS_URL)
    suspend fun getSongs(@QueryMap params: Map<String, String>): Response<SearchResponse>
}