package com.jeanpaulo.musiclibrary.app

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class MainFragmentEnum {
    object PlaylistFragment : MainFragmentEnum()
    object FavoritesFragment : MainFragmentEnum()
    object SearchFragment : MainFragmentEnum()
}

sealed class MusicActivityState {
    class OpenMusicDetail(val view: View, val music: SimpleMusicDetailUIModel) : MusicActivityState()
}

class MainViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
) : BaseViewModel() {

    private val _actMusicFragment = MutableLiveData<MainFragmentEnum>()
    val actMusicFragment: LiveData<MainFragmentEnum> get() = _actMusicFragment

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