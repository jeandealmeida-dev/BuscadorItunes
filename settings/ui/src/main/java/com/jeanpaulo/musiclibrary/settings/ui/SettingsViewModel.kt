package com.jeanpaulo.musiclibrary.settings.ui

import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor

import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val favoriteInteractor: FavoriteInteractor
) : BaseViewModel() {

    fun clearDatabase() {
        compositeDisposable.add(
            favoriteInteractor.deleteAll()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe()
        )
    }
}