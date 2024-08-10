package com.jeanpaulo.musiclibrary.core.repository.database.entity

import org.junit.Assert.assertEquals
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import org.junit.Test

class CollectionEntityTest {

    @Test
    fun `GIVEN a Collection model WHEN converting to entity THEN entity should have correct values`() {
        // GIVEN
        val collection = Collection(
            collectionId = 1L,
            name = "Collection Name"
        )

        // WHEN
        val collectionEntity = CollectionEntity.from(collection)

        // THEN
        assertEquals(collection.collectionId, collectionEntity.collectionId)
        assertEquals(collection.name, collectionEntity.name)
    }

    @Test
    fun `GIVEN a CollectionEntity WHEN converting to model THEN model should have correct values`() {
        // GIVEN
        val collectionEntity = CollectionEntity(
            collectionId = 1L,
            name = "Collection Name"
        )

        // WHEN
        val collectionModel = collectionEntity.toModel()

        // THEN
        assertEquals(collectionEntity.collectionId, collectionModel.collectionId)
        assertEquals(collectionEntity.name, collectionModel.name)
    }

    @Test
    fun `GIVEN a CollectionEntity WHEN created THEN values should be set correctly`() {
        // GIVEN
        val collectionId = 1L
        val name = "Collection Name"

        // WHEN
        val collectionEntity = CollectionEntity(
            collectionId = collectionId,
            name = name
        )

        // THEN
        assertEquals(collectionId, collectionEntity.collectionId)
        assertEquals(name, collectionEntity.name)
    }
}