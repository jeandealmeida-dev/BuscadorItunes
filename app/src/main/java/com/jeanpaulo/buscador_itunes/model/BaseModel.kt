package com.jeanpaulo.buscador_itunes.model

abstract class BaseModel {

    abstract var origin: Origin

    enum class Origin {
        UNDEF,
        LOCAL,
        REMOTE
    }
}