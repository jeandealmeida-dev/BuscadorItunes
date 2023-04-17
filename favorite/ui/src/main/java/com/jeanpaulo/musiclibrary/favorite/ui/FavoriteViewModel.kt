package com.jeanpaulo.musiclibrary.favorite.ui

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
    data class Success(val musicList: List<Music>) : FavoriteState()
}

class FavoriteViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

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
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    val musicFiltered = favorites.map { it.music }
                    _favoriteState.postValue(FavoriteState.Success(musicFiltered))
                }, {
                    if (it is EmptyResultException) {
                        _favoriteState.postValue(FavoriteState.Success(emptyList()))
                    } else {
                        _favoriteState.postValue(FavoriteState.Error)
                    }
                })
        )
    }
}