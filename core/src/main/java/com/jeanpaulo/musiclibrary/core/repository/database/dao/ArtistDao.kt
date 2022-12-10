package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity

@Dao
interface ArtistDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtist(music: ArtistEntity): kotlin.Long

    //R

    @Query("SELECT * FROM artist")
    fun getArtists(): List<ArtistEntity>

    @Query("SELECT * FROM artist WHERE artistId = :id")
    fun getArtistById(id: Long): ArtistEntity?

    //U

    @Update
    fun updateArtist(music: ArtistEntity): Int
    //D

    @Query("DELETE FROM artist WHERE artistId = :id")
    fun deleteArtistById(id: kotlin.Long): Int

    @Query("DELETE FROM artist")
    fun deleteArtists(): kotlin.Int

}