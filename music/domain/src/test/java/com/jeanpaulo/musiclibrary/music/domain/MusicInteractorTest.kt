package com.jeanpaulo.musiclibrary.music.domain

import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.music.data.MusicRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class MusicInteractorTest {

    private lateinit var interactor: MusicInteractor

    @MockK
    private lateinit var repository: MusicRepository

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        interactor = MusicInteractorImpl(repository, favoriteRepository)
    }

    // getMusic

    @Test
    fun `GIVEN a valid music id WHEN getMusic from api THEN it should return the expected music`() {
        // GIVEN
        val musicId = 1L
        every { repository.lookup(musicId = musicId, MusicInteractor.SONG) } returns Single.just(
            music1
        )

        // WHEN
        interactor.getMusic(musicId)
            // THEN
            .test().assertValue(music1)
    }

    @Test
    fun `GIVEN a invalid music id WHEN getMusic from api THEN it should return an exception`() {
        // GIVEN
        val musicId = -1L
        every { repository.lookup(musicId = musicId, MusicInteractor.SONG) } returns Single.error(
            exception
        )

        // WHEN
        interactor.getMusic(musicId)
            // THEN
            .test().assertError(exception)
    }

    // findLocal

    @Test
    fun `GIVEN a remote music id WHEN findLocal from database THEN it should return the expected music`() {
        // GIVEN
        val musicId = 1L
        every { repository.findLocal(remoteId = musicId) } returns Single.just(
            music1
        )

        // WHEN
        interactor.findLocal(musicId)
            // THEN
            .test().assertValue(music1)
    }

    @Test
    fun `GIVEN a remote music id WHEN findLocal from database and doesn't find it THEN it should return empty exception`() {
        // GIVEN
        val musicId = 1L
        every { repository.findLocal(remoteId = musicId) } returns Single.error(
            emptyException
        )

        // WHEN
        interactor.findLocal(musicId)
            // THEN
            .test().assertError(emptyException)
    }

    @Test
    fun `GIVEN a invalid remote music id WHEN findLocal from database THEN it should return an exception`() {
        // GIVEN
        val musicId = -1L
        every { repository.findLocal(remoteId = musicId) } returns Single.error(
            exception
        )

        // WHEN
        interactor.findLocal(musicId)
            // THEN
            .test().assertError(exception)
    }

    // removeFavorite

    @Test
    fun `GIVEN favorite music id WHEN removeFromFavorites from database THEN it should return complete`() {
        // GIVEN
        val musicId = 1L
        every { favoriteRepository.remove(musicId = musicId) } returns Completable.complete()

        // WHEN
        interactor.removeFromFavorites(musicId)
            // THEN
            .test().assertComplete()
    }

    // isFavorite

    @Test
    fun `GIVEN a valid music id WHEN called isFavorite THEN it should return if it is favorite or not`() {
        // GIVEN
        val musicId = -1L
        val isFavorite = true

        every { favoriteRepository.isFavorite(remoteId = musicId) } returns Single.just(
            isFavorite
        )

        // WHEN
        interactor.isFavorite(musicId)
            // THEN
            .test().assertValue(isFavorite)

        val isNotFavorite = false

        every { favoriteRepository.isFavorite(remoteId = musicId) } returns Single.just(
            isNotFavorite
        )

        // WHEN
        interactor.isFavorite(musicId)
            // THEN
            .test().assertValue(isNotFavorite)
    }

    // saveMusicInFavorite

    @Test
    fun `GIVEN a valid music WHEN saveMusicInFavorite local database THEN return complete`(){
        // GIVEN
        val localMusicId = 1L
        every { repository.save(any()) } returns Single.just(localMusicId)
        every { favoriteRepository.save(localMusicId) } returns Completable.complete()

        // WHEN
        interactor.saveMusicInFavorite(music1)
            // THEN
            .test()
            .assertComplete()
    }

}