package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Collection

@Entity(tableName = "collection")
data class CollectionEntity(
    @PrimaryKey @ColumnInfo(name = "collectionId") val collectionId: Long,
    @ColumnInfo(name = "name") val name: String?
) {
    fun toModel(): Collection =  Collection(collectionId, name)
}