package com.jeanpaulo.musiclibrary.music.data

import com.jeanpaulo.musiclibrary.core.repository.database.dao.ArtistDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.CollectionDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.MusicDao
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class MusicRepositoryTest {

    @MockK
    private lateinit var service: ItunesService

    @MockK
    private lateinit var musicDao: MusicDao

    @MockK
    private lateinit var artistDao: ArtistDao

    @MockK
    private lateinit var collectionDao: CollectionDao

    private lateinit var repository: MusicRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = MusicRepositoryImpl(service, musicDao, artistDao, collectionDao)
    }


    // lookup

    @Test
    fun `GIVEN a invalid remote music id WHEN lookUp from api THEN should return the empty exception`() {
        // GIVEN
        val remoteId = -1L
        every { service.lookUp(remoteId, queryEntity) } returns Single.error(emptyException)

        // WHEN
        repository.lookup(remoteId, queryEntity)
            // THEN
            .test()
            .assertError(emptyException)
    }

    @Test
    fun `GIVEN a valid remote music id WHEN lookUp from api THEN should return a correspondent music`() {
        // GIVEN
        val remoteId = -1L
        val music = musicResponse1.toModel()
        every { service.lookUp(remoteId, queryEntity) } returns Single.just(lookUpResponse)

        // WHEN
        repository.lookup(remoteId, queryEntity)
            // THEN
            .test()
            .assertValue(music)
    }

    // save

    @Test
    fun `GIVEN a valid music with artist and collection WHEN save a music in local storage THEN should return a valid local music id`() {
        // GIVEN
        every { artistDao.insertArtist(artist1Entity) } returns Single.just(localArtistId)
        every { collectionDao.insertCollection(collection1Entity) } returns Single.just(
            localCollectionId
        )
        every { musicDao.insertMusic(music1Entity) } returns Single.just(localMusicId)

        music1Entity.apply {
            artist = artist1Entity
            collection = collection1Entity
        }

        // WHEN
        repository.save(music1Entity)
            // THEN
            .test()
            .assertValue(localMusicId)

        verify(exactly = 1) { artistDao.insertArtist(artist1Entity) }
        verify(exactly = 1) { collectionDao.insertCollection(collection1Entity) }
    }


    @Test
    fun `GIVEN a valid music WHEN save a music in local storage THEN should return a valid local music id`() {
        // GIVEN
        every { musicDao.insertMusic(music2Entity) } returns Single.just(localMusicId)

        // WHEN
        repository.save(music2Entity)
            // THEN
            .test()
            .assertValue(localMusicId)

        verify(exactly = 0) { artistDao.insertArtist(artist2Entity) }
        verify(exactly = 0) { collectionDao.insertCollection(collection2Entity) }
    }

    // get
    @Test
    fun `GIVEN a music WHEN get music from local storage THEN should return empty exception`() {
        // GIVEN
        val musicId = 1L
        every { musicDao.getMusicById(musicId) } returns Single.just(music1Entity)
        every { artistDao.getArtistById(music1Entity.artistId) } returns Single.just(artist1Entity)
        every { collectionDao.getCollectionById(music1Entity.collectionId) } returns Single.just(
            collection1Entity
        )

        music1Entity.apply {
            artist = artist1Entity
            collection = collection1Entity
        }

        // WHEN
        repository.get(musicId)
            // THEN
            .test()
            .assertResult(music1)

        verify(exactly = 1) { artistDao.getArtistById(music1Entity.artistId) }
        verify(exactly = 1) { collectionDao.getCollectionById(music1Entity.collectionId) }
    }

    @Test
    fun `GIVEN a music that is not saved WHEN get music from local storage THEN should return empty exception`() {
        // GIVEN
        val musicId = 1L
        every { musicDao.getMusicById(musicId) } returns Single.error(emptyException)

        // WHEN
        repository.get(musicId)
            // THEN
            .test()
            .assertError(emptyException)
    }

    @Test
    fun `GIVEN an error WHEN get music from local storage THEN should return exception`() {
        // GIVEN
        val musicId = 1L
        every { musicDao.getMusicById(musicId) } returns Single.error(exception)

        // WHEN
        repository.get(musicId)
            // THEN
            .test()
            .assertError(exception)
    }

}