package com.jeanpaulo.buscador_itunes.music.data.local.dao

import androidx.room.*
import com.jeanpaulo.buscador_itunes.music.data.local.model.ArtistEntity

@Dao
interface ArtistDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(music: ArtistEntity): kotlin.Long

    //R

    @Query("SELECT * FROM artist")
    suspend fun getArtists(): List<ArtistEntity>

    @Query("SELECT * FROM artist WHERE artistId = :id")
    suspend fun getArtistById(id: Long): ArtistEntity?

    //U

    @Update
    suspend fun updateArtist(music: ArtistEntity): Int
    //D

    @Query("DELETE FROM artist WHERE artistId = :id")
    suspend fun deleteArtistById(id: kotlin.Long): Int

    @Query("DELETE FROM artist")
    suspend fun deleteArtists(): kotlin.Int

}