package com.jeanpaulo.buscador_itunes.app.music.presentation.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class MusicFragmentEnum {
    object PlaylistFragment : MusicFragmentEnum()
    object FavoritesFragment : MusicFragmentEnum()
    object SearchFragment : MusicFragmentEnum()
}

sealed class MusicActivityState {
    class OpenMusicDetail(val view: View, val music: SimpleMusicDetailUIModel) : MusicActivityState()
}

class MusicViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
) : com.jeanpaulo.musiclibrary.commons.base.BaseViewModel() {

    private val _actMusicFragment = MutableLiveData<MusicFragmentEnum>()
    val actMusicFragment: LiveData<MusicFragmentEnum> get() = _actMusicFragment

    private val _state = MutableLiveData<MusicActivityState>()
    val state: LiveData<MusicActivityState> get() = _state

    override fun onCreate() {
        super.onCreate()

        setupWidgets()
    }

    private fun setupWidgets() {

    }

    fun openMusicDetail(view: View, music: SimpleMusicDetailUIModel) {
        _state.value = MusicActivityState.OpenMusicDetail(view, music)
    }
}