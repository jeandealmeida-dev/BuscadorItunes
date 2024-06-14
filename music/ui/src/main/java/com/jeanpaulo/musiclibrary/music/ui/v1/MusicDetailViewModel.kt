package com.jeanpaulo.musiclibrary.music.ui.v1

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.domain.MusicInteractor
import com.jeanpaulo.musiclibrary.music.ui.v1.di.FromRemote
import com.jeanpaulo.musiclibrary.music.ui.v1.di.SimpleMusicUI
import com.jeanpaulo.musiclibrary.music.ui.model.MusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.model.mapper.convertToMusicUI
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    class Success(val isFavorite: Boolean) : FavoriteState()
    object Loading : FavoriteState()
    object Error : FavoriteState()
}

sealed class MusicPlayerState {
    object Init : MusicPlayerState()
    class Play(val uri: String) : MusicPlayerState()
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
    private val interactor: MusicInteractor,
    @SimpleMusicUI val simpleMusicDetail: SimpleMusicDetailUIModel,
    @FromRemote val fromRemote: Boolean,
) : BaseViewModel() {

    private var _isFavorited = false
    val isFavorite: Boolean get() = _isFavorited

    private lateinit var _music: Music
    val musicDetailUIModel: MusicDetailUIModel get() = _music.convertToMusicUI()

    private val _musicDetailState = MutableLiveData<MusicDetailState>()
    val musicDetailState: LiveData<MusicDetailState> get() = _musicDetailState

    private val _musicPlayerState = MutableLiveData<MusicPlayerState>()
    val musicPlayerState: LiveData<MusicPlayerState> get() = _musicPlayerState

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState

    override fun onStart() {
        super.onStart()
        getDetail()
    }

    fun getDetail() {
        if (fromRemote)
            getMusicDetail(simpleMusicDetail.id)
        else
            findLocalMusicDetail(simpleMusicDetail.id)
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

    private fun findLocalMusicDetail(musicId: Long) {
        compositeDisposable.add(
            interactor.findLocal(musicId)
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

    fun setupMusicDetail(music: Music) {
        _music = music

        isFavorited(music.ds_trackId ?: 0L)

        _musicPlayerState.value = MusicPlayerState.Init
    }

    private fun isFavorited(remoteId: Long) {
        compositeDisposable.add(
            interactor.isFavorite(remoteId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.value = FavoriteState.Loading
                }
                //.delay(600, TimeUnit.MILLISECONDS)
                .subscribe({ it ->
                    _isFavorited = it
                    _favoriteState.value = FavoriteState.Success(it)
                }, {
                    if (it is EmptyResultException) {
                        _isFavorited = false
                        _favoriteState.value = FavoriteState.Success(_isFavorited)
                    } else {
                        _favoriteState.value = FavoriteState.Error
                    }
                })
        )
    }

    private fun saveInFavorite() {
        compositeDisposable.add(
            interactor.saveMusicInFavorite(_music)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.value = FavoriteState.Loading
                }
                //.delay(600, TimeUnit.MILLISECONDS)
                .subscribe({
                    _isFavorited = true
                    _favoriteState.value = FavoriteState.Success(_isFavorited)
                }, {
                    _favoriteState.value = FavoriteState.Error
                })
        )
    }

//    private fun saveInFavorite(musicId: Long) {
//        compositeDisposable.add(
//            favoriteInteractor.saveInFavorite(musicId)
//                .subscribeOn(ioScheduler)
//                .observeOn(mainScheduler)
//                .doOnSubscribe {
//                    _favoriteState.value = FavoriteState.Loading
//                }
//                //.delay(600, TimeUnit.MILLISECONDS)
//                .subscribe({
//                    _favoriteState.value = FavoriteState.Success(true)
//                    _musicDetailUIModel.isFavorite()
//                }, {
//                    _favoriteState.value = FavoriteState.Error
//                })
//        )
//    }

    private fun removeFavorite() {
        compositeDisposable.add(
            interactor.removeFromFavorites(musicDetailUIModel.remoteId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.value = FavoriteState.Loading
                }
                //.delay(600, TimeUnit.MILLISECONDS)
                .subscribe({
                    _isFavorited = false
                    _favoriteState.value = FavoriteState.Success(_isFavorited)
                }, {
                    _favoriteState.value = FavoriteState.Error
                })
        )
    }

    fun clickFavoriteMenu() {
        when (isFavorite) {
            true -> removeFavorite()
            false -> saveInFavorite()
        }
    }

    fun changePlayerState() {
        when (musicPlayerState.value) {
            is MusicPlayerState.Play -> {
                _musicPlayerState.value = MusicPlayerState.Stop
            }
            MusicPlayerState.Init,
            MusicPlayerState.Stop -> {
                _musicPlayerState.value = MusicPlayerState.Play(_music.previewUrl ?: "")
            }
            else -> {}
        }
    }


}