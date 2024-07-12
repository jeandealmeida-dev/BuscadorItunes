package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection

@Entity(tableName = CollectionEntity.TABLE)
data class CollectionEntity(
    @PrimaryKey @ColumnInfo(name = COLLECTION_ID) val collectionId: Long,
    @ColumnInfo(name = NAME) val name: String
) {

    fun toModel() = Collection(
        collectionId = collectionId,
        name = name,
    )

    companion object {
        const val TABLE = "collection"

        const val COLLECTION_ID = "collectionId"
        const val NAME = "name"

        fun from(collection: Collection) = CollectionEntity(
            collectionId = collection.collectionId,
            name = collection.name,
        )
    }
}