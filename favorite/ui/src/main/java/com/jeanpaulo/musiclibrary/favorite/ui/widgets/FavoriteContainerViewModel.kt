package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class FavoriteContainerViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

    private val _favoriteCountState = MutableLiveData<ViewState<Int>>()
    val favoriteCountState: LiveData<ViewState<Int>> get() = _favoriteCountState

    fun getFavoriteCount() {
        compositeDisposable.add(
            interactor.getFavoriteCount()
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _favoriteCountState.postValue(ViewState.Loading)
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ count ->
                    _favoriteCountState.postValue(ViewState.Success(count))
                }, {
                    if (it is EmptyResultException) {
                        _favoriteCountState.postValue(ViewState.Empty)
                    } else {
                        _favoriteCountState.postValue(ViewState.Error)
                    }
                })
        )
    }
}
