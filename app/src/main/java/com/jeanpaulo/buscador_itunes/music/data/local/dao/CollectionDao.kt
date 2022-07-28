package com.jeanpaulo.buscador_itunes.music.data.local.dao

import androidx.room.*
import com.jeanpaulo.buscador_itunes.music.data.local.model.CollectionEntity

@Dao
interface CollectionDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(music: CollectionEntity): kotlin.Long

    //R

    @Query("SELECT * FROM collection")
    suspend fun getCollections(): List<CollectionEntity>

    @Query("SELECT * FROM collection WHERE collectionId = :id")
    suspend fun getCollectionById(id: Long): CollectionEntity?

    //U

    @Update
    suspend fun updateCollection(music: CollectionEntity): Int
    //D

    @Query("DELETE FROM collection WHERE collectionId = :id")
    suspend fun deleteCollectionById(id: kotlin.Long): Int

    @Query("DELETE FROM collection")
    suspend fun deleteCollections(): kotlin.Int

}