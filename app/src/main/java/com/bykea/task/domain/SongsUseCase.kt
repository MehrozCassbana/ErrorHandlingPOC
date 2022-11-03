package com.bykea.task.domain

import com.bykea.task.data.remote.ApiParams.TERM
import javax.inject.Inject

class SongsUseCase @Inject constructor(private val songsRepository: SongsRepository) {

    val searchResponse = songsRepository.searchResponse

    suspend fun searchSongs(artistName: String) {
        val params: Map<String, String> = mapOf(
            TERM to artistName.replace(" ","+")
        )
        songsRepository.searchSongs(params)
    }
}