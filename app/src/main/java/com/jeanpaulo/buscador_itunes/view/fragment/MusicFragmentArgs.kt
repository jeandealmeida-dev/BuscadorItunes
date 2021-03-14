package com.jeanpaulo.buscador_itunes.view.fragment

import android.os.Bundle
import androidx.navigation.NavArgs

data class MusicFragmentArgs(
    val userMessage: Int = 0
) : NavArgs {
    fun toBundle(): Bundle {
        val result = Bundle()
        result.putInt("userMessage", this.userMessage)
        return result
    }

    companion object {
        @JvmStatic
        fun fromBundle(bundle: Bundle): MusicFragmentArgs {
            bundle.setClassLoader(MusicFragmentArgs::class.java.classLoader)
            val __userMessage: Int
            if (bundle.containsKey("userMessage")) {
                __userMessage = bundle.getInt("userMessage")
            } else {
                __userMessage = 0
            }
            return MusicFragmentArgs(
                __userMessage
            )
        }
    }
}
