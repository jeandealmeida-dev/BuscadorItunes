package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Collection

@Entity(tableName = CollectionEntity.TABLE)
data class CollectionEntity(
    @PrimaryKey @ColumnInfo(name = ID) val id: Long,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object {
        const val TABLE = "collection"
        const val ID = "collectionId"
        const val NAME = "name"
    }
}