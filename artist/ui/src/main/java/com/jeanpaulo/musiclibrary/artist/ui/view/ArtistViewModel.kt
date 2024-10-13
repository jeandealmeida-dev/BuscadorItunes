package com.jeanpaulo.musiclibrary.artist.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.artist.domain.ArtistInteractor
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArtistViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val interactor: ArtistInteractor
) : BaseViewModel() {

    private val _artistState = MutableLiveData<ViewState<Artist>>()
    val artistState: LiveData<ViewState<Artist>> get() = _artistState

    fun getArtist(artistId: Long) {
        compositeDisposable.add(
            interactor.getArtist(artistId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _artistState.postValue(ViewState.Loading)
                }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe({ artist ->
                    _artistState.postValue(ViewState.Success(artist))
                }, {
                    _artistState.postValue(ViewState.Error)
                    it.printStackTrace()
                })
        )
    }
}