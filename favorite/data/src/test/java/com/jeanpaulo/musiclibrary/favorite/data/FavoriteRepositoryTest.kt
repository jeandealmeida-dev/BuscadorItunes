package com.jeanpaulo.musiclibrary.favorite.data

import androidx.room.rxjava3.EmptyResultSetException
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.repository.database.dao.FavoriteDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {

    @MockK
    private lateinit var favoriteDao: FavoriteDao

    private lateinit var favoriteRepository: FavoriteRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        favoriteRepository = FavoriteRepositoryImpl(favoriteDao)
    }

    @Test
    fun `GIVEN a valid musicId WHEN saving favorite THEN dao insert should be called`() {
        // GIVEN
        val musicId = 1L
        every { favoriteDao.insert(any()) } returns Completable.complete()

        // WHEN
        val testObserver = favoriteRepository.save(musicId).test()

        // THEN
        verify { favoriteDao.insert(any()) }
        testObserver.assertComplete()
    }

    @Test
    fun `GIVEN a valid remoteId WHEN checking if favorite THEN return true if exists`() {
        // GIVEN
        val remoteId = 1L
        every { favoriteDao.isFavorite(remoteId) } returns Single.just(1)

        // WHEN
        val testObserver = favoriteRepository.isFavorite(remoteId).test()

        // THEN
        testObserver.assertValue(true)
        verify { favoriteDao.isFavorite(remoteId) }
    }

    @Test
    fun `GIVEN a valid remoteId WHEN checking if favorite THEN return false if not exists`() {
        // GIVEN
        val remoteId = 1L
        every { favoriteDao.isFavorite(remoteId) } returns Single.just(0)

        // WHEN
        val testObserver = favoriteRepository.isFavorite(remoteId).test()

        // THEN
        testObserver.assertValue(false)
        verify { favoriteDao.isFavorite(remoteId) }
    }

    @Test
    fun `GIVEN a database error WHEN checking if favorite THEN return EmptyResultException`() {
        // GIVEN
        val remoteId = 1L
        every { favoriteDao.isFavorite(remoteId) } returns Single.error(EmptyResultSetException("No data found"))

        // WHEN
        val testObserver = favoriteRepository.isFavorite(remoteId).test()

        // THEN
        testObserver.assertError(EmptyResultException::class.java)
        verify { favoriteDao.isFavorite(remoteId) }
    }

    @Test
    fun `GIVEN dao returns list of favorites WHEN getting all THEN list should be mapped to model`() {
        // GIVEN
        val favoriteEntities = listOf(FavoriteEntity(musicId = 1L))
        every { favoriteDao.getFavorites() } returns Flowable.just(favoriteEntities)

        // WHEN
        val testObserver = favoriteRepository.getAll().test()

        // THEN
        testObserver.assertValue { favorites ->
            favorites.size == favoriteEntities.size && favorites[0].musicId == favoriteEntities[0].musicId
        }
        verify { favoriteDao.getFavorites() }
    }

    @Test
    fun `GIVEN dao returns count WHEN getting count THEN return the count`() {
        // GIVEN
        val count = 5
        every { favoriteDao.getCount() } returns Single.just(count)

        // WHEN
        val testObserver = favoriteRepository.getCount().test()

        // THEN
        testObserver.assertValue(count)
        verify { favoriteDao.getCount() }
    }

    @Test
    fun `GIVEN a valid remoteId WHEN removing favorite THEN dao remove should be called`() {
        // GIVEN
        val remoteId = 1L
        every { favoriteDao.removeMusicFromFavorite(remoteId) } returns Completable.complete()

        // WHEN
        val testObserver = favoriteRepository.remove(remoteId).test()

        // THEN
        verify { favoriteDao.removeMusicFromFavorite(remoteId) }
        testObserver.assertComplete()
    }
}
