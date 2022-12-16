package com.jeanpaulo.musiclibrary.music.detail.presentation.viewmodel

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.detail.di.SimpleMusicUI
import com.jeanpaulo.musiclibrary.music.detail.domain.MusicDetailInteractor
import com.jeanpaulo.musiclibrary.music.detail.presentation.model.MusicDetailUIModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class MusicPlayerState {
    class Setup(val uri: String) : MusicPlayerState()
    object Play : MusicPlayerState()
    object Stop : MusicPlayerState()
}

sealed class MusicDetailState {
    object Loading : MusicDetailState()
    object Error : MusicDetailState()
    object Success : MusicDetailState()
}

class MusicDetailViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: MusicDetailInteractor,
    @SimpleMusicUI val music: SimpleMusicDetailUIModel,
) : BaseViewModel() {

    private val _musicDetailUIModel = MutableLiveData<MusicDetailUIModel>()
    val musicDetailUIModel: LiveData<MusicDetailUIModel> get() = _musicDetailUIModel

    private val _musicDetailState = MutableLiveData<MusicDetailState>()
    val musicDetailState: LiveData<MusicDetailState> get() = _musicDetailState

    private val _musicPlayerState = MutableLiveData<MusicPlayerState>()
    val musicPlayerState: LiveData<MusicPlayerState> get() = _musicPlayerState

    override fun onCreate() {
        super.onCreate()
        getMusicDetail(music.id)
    }

    private fun getMusicDetail(musicId: Long) {
        compositeDisposable.add(
            interactor.getMusic(musicId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    setupMusicDetail(it)
                    _musicDetailState.value = MusicDetailState.Success
                }, {
                    it.message
                    _musicDetailState.value = MusicDetailState.Error
                })
        )
    }

    fun setupMusicDetail(musicDetail: MusicDetailUIModel){
        _musicDetailUIModel.value = musicDetail
        if(musicDetail.hasPreview()){
            _musicPlayerState.value = MusicPlayerState.Setup(musicDetail.getPreview())
        }
    }

//    private fun isFavorited(musicId: Long){
//        compositeDisposable.add(
//            interactor.isFavorited(musicId)
//                .subscribeOn(ioScheduler)
//                .observeOn(mainScheduler)
//                .doOnSubscribe {
//                    stateLiveData.postValue(MusicDetailState.Loading)
//                }.delay(600, TimeUnit.MILLISECONDS)
//                .subscribe({
//                    isFavoritedLiveData.postValue(it)
//                }, {
//                    isFavoritedLiveData.postValue(false)
//                    stateLiveData.postValue(MusicDetailState.Error)
//                })
//        )
//    }


//    private fun favorite() {
//        musicLiveData.value?.let { interactor.saveFavorite(it) }
//    }


    fun refresh() {
        //searchMusic()
    }

    fun favoriteChanged() {
        //favorite()
    }

    fun changePlayerState() {
        when (musicPlayerState.value) {
            MusicPlayerState.Play -> {
                _musicPlayerState.value = MusicPlayerState.Stop
            }
            is MusicPlayerState.Setup,
            MusicPlayerState.Stop -> {
                _musicPlayerState.value = MusicPlayerState.Play
            }
            else -> {}
        }
    }
}