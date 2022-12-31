package com.jeanpaulo.musiclibrary.favorite.presentation.viewmodel

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    object Loading : FavoriteState()
    object Error : FavoriteState()
    object Success : FavoriteState()
}

class FavoriteViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

    private var _musicList: MutableLiveData<List<Music>> = MutableLiveData()
    val musicList: LiveData<List<Music>> = _musicList

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState

    override fun onCreate() {
        super.onCreate()
        getFavoriteList()
    }

    fun refresh() {
        getFavoriteList()
    }

    fun getFavoriteList() {
        compositeDisposable.add(
            interactor.getFavoriteMusics()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.postValue(FavoriteState.Loading)
                }
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    val musicFiltered = favorites.map { it.music!! }
                    _musicList.postValue(musicFiltered)
                    _favoriteState.postValue(FavoriteState.Success)
                }, {
                    if (it is EmptyResultException) {
                        _favoriteState.postValue(FavoriteState.Success)
                    } else {
                        _favoriteState.postValue(FavoriteState.Error)
                    }
                })
        )
    }

    fun removeMusicFromFavorite(trackId: Long) {
        compositeDisposable.add(
            interactor.removeFromFavorites(trackId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.postValue(FavoriteState.Loading)
                }
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe({
                    _favoriteState.postValue(FavoriteState.Success)
                }, {
                    _favoriteState.postValue(FavoriteState.Error)
                })
        )
    }
}