package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollection(music: CollectionEntity): kotlin.Long

    @Query("SELECT * FROM collection")
    fun getCollections(): List<CollectionEntity>

    @Query("SELECT * FROM collection WHERE collectionId = :id")
    fun getCollectionById(id: Long): CollectionEntity?

    @Update
    fun updateCollection(music: CollectionEntity): Int

    @Query("DELETE FROM collection WHERE collectionId = :id")
    fun deleteCollectionById(id: kotlin.Long): Int

    @Query("DELETE FROM collection")
    fun deleteCollections(): kotlin.Int
}