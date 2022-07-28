package com.jeanpaulo.buscador_itunes.music.data.local.dao

import androidx.room.*
import com.jeanpaulo.buscador_itunes.music.data.local.model.MusicEntity

@Dao
interface MusicDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicEntity): kotlin.Long

    //R

    @Query("SELECT * FROM Music")
    suspend fun getMusics(): List<MusicEntity>

    @Query("SELECT * FROM Music WHERE musicId = :id")
    suspend fun getMusicById(id: Long): MusicEntity?

    //U

    @Update
    suspend fun updateMusic(music: MusicEntity): Int
    //D

    @Query("DELETE FROM music WHERE musicId = :id")
    suspend fun deleteMusicById(id: kotlin.Long): Int

    @Query("DELETE FROM music")
    suspend fun deleteMusics(): kotlin.Int

}