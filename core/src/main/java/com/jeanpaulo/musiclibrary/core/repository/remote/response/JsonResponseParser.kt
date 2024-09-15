package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.jeanpaulo.musiclibrary.commons.exceptions.CastException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlin.reflect.KClass

class JsonResponseParser @Inject constructor(
    val moshi: Moshi
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> parse(map: Any?, type: KClass<T>): T? {
        (map as? Map<String, Any>)?.let { map ->
            val json = moshi.adapter(Map::class.java).toJson(map)
            val adapter: JsonAdapter<T> = moshi.adapter(type.java)
            return adapter.fromJson(json)
        } ?: throw CastException()
    }
}