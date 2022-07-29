package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

abstract class BaseModel {

    abstract var origin: Origin

    enum class Origin {
        UNDEF,
        LOCAL,
        REMOTE
    }
}