package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity

@Dao
interface MusicDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(music: MusicEntity): kotlin.Long

    //R

    @Query("SELECT * FROM Music")
    fun getMusics(): List<MusicEntity>

    @Query("SELECT * FROM Music WHERE musicId = :id")
    fun getMusicById(id: Long): MusicEntity?

    @Query("SELECT * FROM Music WHERE remoteMusicId = :remoteMusicId")
    fun getMusicByRemoteId(remoteMusicId: Long): MusicEntity?

    //U

    @Update
    fun updateMusic(music: MusicEntity): Int
    //D

    @Query("DELETE FROM music WHERE musicId = :id")
    fun deleteMusicById(id: kotlin.Long): Int

    @Query("DELETE FROM music")
    fun deleteMusics(): kotlin.Int

}