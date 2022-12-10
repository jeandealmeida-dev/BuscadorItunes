package com.jeanpaulo.musiclibrary.commons.ref

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

class ReactiveProgramming {

    val result = (1..5).filter { it % 2 == 0 } // 2,4


    fun main() = runBlocking {
        print("Flow")
        delayedRange.collect { println("${it}") }
    }

    val delayedRange = (1..5).asFlow().onEach {
        print("delay")
        delay(500)
    }

    val sum: Int = (1..5).reduce { acc, i -> acc + i }

    fun exemplo(bool:Boolean, action2: String.() -> Unit ){
        val result = action2("jeje")
    }
}