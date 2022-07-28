package com.jeanpaulo.buscador_itunes.music.domain.model

abstract class BaseModel {

    abstract var origin: Origin

    enum class Origin {
        UNDEF,
        LOCAL,
        REMOTE
    }
}