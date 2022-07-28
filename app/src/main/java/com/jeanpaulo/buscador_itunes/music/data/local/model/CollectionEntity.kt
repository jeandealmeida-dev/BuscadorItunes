package com.jeanpaulo.buscador_itunes.music.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.music.domain.model.Collection

@Entity(tableName = "collection")
data class CollectionEntity(
    @PrimaryKey @ColumnInfo(name = "collectionId") val collectionId: Long,
    @ColumnInfo(name = "name") val name: String?
) {
    fun toModel(): Collection =  Collection(collectionId, name)
}