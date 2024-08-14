package com.jeanpaulo.musiclibrary.favorite.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.music.data.MusicRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class FavoriteInteractorImplTest {

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @MockK
    private lateinit var musicRepository: MusicRepository

    private lateinit var favoriteInteractor: FavoriteInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        favoriteInteractor = FavoriteInteractorImpl(favoriteRepository, musicRepository)
    }

    @Test
    fun `GIVEN a valid dsTrackid WHEN checking if favorite THEN return correct value`() {
        // GIVEN
        val dsTrackid = 1L
        every { favoriteRepository.isFavorite(dsTrackid) } returns Single.just(true)

        // WHEN
        val testObserver = favoriteInteractor.isFavorite(dsTrackid).test()

        // THEN
        testObserver.assertValue(true)
        verify { favoriteRepository.isFavorite(dsTrackid) }
    }

    @Test
    fun `GIVEN a valid music WHEN saving to favorite THEN repository save should be called`() {
        // GIVEN
        val music = Music(
            musicId = 1L,
            trackName = "Track Name",
            artworkUrl = "url",
            releaseDate = null,
            streamable = true,
            trackTimeMillis = 300000,
            previewUrl = "previewUrl"
        ).apply {
            musicArtist = Artist(artistId = 2L, name = "Artist Name")
            musicCollection = Collection(collectionId = 3L, name = "Collection Name")
        }

        every { musicRepository.save(any()) } returns Single.just(1L)
        every { favoriteRepository.save(1L) } returns Completable.complete()

        // WHEN
        val testObserver = favoriteInteractor.saveInFavorite(music).test()

        // THEN
        testObserver.assertComplete()
        verify { musicRepository.save(any()) }
        verify { favoriteRepository.save(1L) }
    }

    @Test
    fun `GIVEN a valid musicId WHEN removing from favorite THEN repository remove should be called`() {
        // GIVEN
        val musicId = 1L
        every { favoriteRepository.remove(musicId) } returns Completable.complete()

        // WHEN
        val testObserver = favoriteInteractor.removeFromFavorites(musicId).test()

        // THEN
        testObserver.assertComplete()
        verify { favoriteRepository.remove(musicId) }
    }

    @Test
    fun `GIVEN favorite list WHEN getting favorite musics THEN map to correct models`() {
        // GIVEN
        val favoriteList = listOf(Favorite(musicId = 1L))
        val music = Music(
            musicId = 1L,
            trackName = "Track Name",
            artworkUrl = "url",
            releaseDate = null,
            streamable = true,
            trackTimeMillis = 300000,
            previewUrl = "previewUrl"
        )

        every { favoriteRepository.getAll() } returns Flowable.just(favoriteList)
        every { musicRepository.get(1L) } returns Single.just(music)

        // WHEN
        val testObserver = favoriteInteractor.getFavoriteMusics().test()

        // THEN
        testObserver.assertValue { favorites ->
            favorites.size == 1 && favorites[0].music?.musicId == 1L
        }
        verify { favoriteRepository.getAll() }
        verify { musicRepository.get(1L) }
    }

    @Test
    fun `GIVEN favorite count WHEN getting favorite count THEN return correct value`() {
        // GIVEN
        val count = 5
        every { favoriteRepository.getCount() } returns Single.just(count)

        // WHEN
        val testObserver = favoriteInteractor.getFavoriteCount().test()

        // THEN
        testObserver.assertValue(count)
        verify { favoriteRepository.getCount() }
    }
}
