package com.jeanpaulo.buscador_itunes.view.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type


class CollectionAdapter :JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (!annotations.isEmpty()) return null

        /*if (typeSupported(type)) {
            val nameToAdapter: MutableMap<String, JsonAdapter<*>> =
                LinkedHashMap()
            val classToAdapter: MutableMap<Class<*>, JsonAdapter<*>> =
                LinkedHashMap()
            for ((key, value) in kindToClass.entrySet()) {
                val adapter: JsonAdapter<Any> =
                    moshi.nextAdapter(this, value, annotations)
                nameToAdapter[key] = adapter
                classToAdapter[value] = adapter
            }
            val adapter =
                GenericPolymorphicJsonAdapter(key, nameToAdapter, classToAdapter)
            return if (lenient) adapter.lenient() else adapter
        }*/

        return null
    }

    private fun typeSupported(type: Type): Boolean {
        /*if (DocPath.parent != null) {
            val rawType: Class<*> = Types.getRawType(type)
            return DocPath.parent.isAssignableFrom(rawType)
        }
        for (cls in kindToClass.values()) if (type === cls) return true*/
        return false
    }

}