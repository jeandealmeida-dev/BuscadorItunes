package com.jeanpaulo.musiclibrary.search.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface SearchInteractor {
    fun getSearchResults(query: String): Flow<PagingData<Music>>
    fun getSearchMusic(musicId: Long): Music?
}

class SearchInteractorImpl @Inject constructor(
    val repository: SearchRepository
) : SearchInteractor {

    private val _musicList = MutableStateFlow<List<Music>>(emptyList())

    override fun getSearchResults(query: String): Flow<PagingData<Music>> =
        repository.getSearchResults(query)
            .map { pagingData ->
                pagingData.map { music ->
                    _musicList.update { current ->
                        current + music
                    }
                    music
                }
            }

    override fun getSearchMusic(musicId: Long): Music? = _musicList.value.find { it.musicId == musicId }
}
