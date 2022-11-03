package com.bykea.task.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.bykea.task.core.base.BaseViewModel
import com.bykea.task.domain.SongsUseCase
import kotlinx.coroutines.launch

class SongViewModel @ViewModelInject constructor(
    private val songsUseCase: SongsUseCase
) : BaseViewModel() {

    val searchResponse = songsUseCase.searchResponse
    fun searchSongs(artistName: String) {
        viewModelScope.launch {
            songsUseCase.searchSongs(artistName)
        }
    }
}