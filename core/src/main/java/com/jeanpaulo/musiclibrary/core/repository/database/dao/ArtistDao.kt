package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtist(music: ArtistEntity): Single<Long>

    @Query(
        "SELECT * " +
                "FROM ${ArtistEntity.TABLE} " +
                "WHERE ${ArtistEntity.ID} = :id"
    )
    fun getArtistById(id: Long): Single<ArtistEntity>

    //R

    @Query(
        "SELECT * " +
                "FROM ${ArtistEntity.TABLE}"
    )
    fun getArtists(): List<ArtistEntity>


    //U

    @Update
    fun updateArtist(music: ArtistEntity): Int
    //D

    @Query(
        "DELETE " +
                "FROM ${ArtistEntity.TABLE} " +
                "WHERE ${ArtistEntity.ID} = :id"
    )
    fun deleteArtistById(id: kotlin.Long): Int

    @Query(
        "DELETE " +
                "FROM ${ArtistEntity.TABLE}"
    )
    fun deleteArtists(): kotlin.Int

}