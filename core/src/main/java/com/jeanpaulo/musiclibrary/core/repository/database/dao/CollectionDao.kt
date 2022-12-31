package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.CollectionEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collection WHERE collectionId = :id")
    fun getCollectionById(id: Long): Single<CollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollection(music: CollectionEntity): Single<Long>

    @Query("SELECT * FROM collection")
    fun getCollections(): List<CollectionEntity>



    @Update
    fun updateCollection(music: CollectionEntity): Int

    @Query("DELETE FROM collection WHERE collectionId = :id")
    fun deleteCollectionById(id: kotlin.Long): Int

    @Query("DELETE FROM collection")
    fun deleteCollections(): kotlin.Int
}