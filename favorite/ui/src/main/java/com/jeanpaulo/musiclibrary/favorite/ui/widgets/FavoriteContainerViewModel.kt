package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteCountState {
    data object Loading : FavoriteCountState()
    data object Error : FavoriteCountState()
    data class Success(val total: Int) : FavoriteCountState()
}
class FavoriteContainerViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

    private val _favoriteCountState = MutableLiveData<FavoriteCountState>()
    val favoriteCountState: LiveData<FavoriteCountState> get() = _favoriteCountState

    fun getFavoriteCount(){
        compositeDisposable.add(
            interactor.getFavoriteCount()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteCountState.postValue(FavoriteCountState.Loading)
                }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe({ count ->
                    _favoriteCountState.postValue(FavoriteCountState.Success(count))
                }, {
                    if (it is EmptyResultException) {
                        _favoriteCountState.postValue(FavoriteCountState.Success(0))
                    } else {
                        _favoriteCountState.postValue(FavoriteCountState.Error)
                    }
                })
        )
    }
}
