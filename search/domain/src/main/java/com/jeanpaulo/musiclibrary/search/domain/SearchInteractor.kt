package com.jeanpaulo.musiclibrary.search.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

interface SearchInteractor {
    fun getSearchResults(query: String): Flowable<PagingData<Music>>
    fun getSearchMusic(musicId: Long): Music?
    fun getSearchMusicList(): List<Music>
}

class SearchInteractorImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchInteractor {

    private val _musicList = BehaviorSubject.createDefault<List<Music>>(emptyList())


    override fun getSearchResults(query: String): Flowable<PagingData<Music>> =
        repository.getSearchResults(query)
            .map { pagingData ->
                pagingData.map { music ->
                    updateMusicList(_musicList.value?.plus(music) ?: listOf(music))
                    music
                }
            }

    override fun getSearchMusic(musicId: Long): Music? =
        _musicList.value?.find { it.musicId == musicId }

    override fun getSearchMusicList(): List<Music> = _musicList.value ?: emptyList()

    fun updateMusicList(musicList: List<Music>) {
        _musicList.onNext(musicList)
    }
}
