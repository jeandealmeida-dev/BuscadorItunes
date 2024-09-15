package com.jeanpaulo.musiclibrary.commons.delegates

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ActivityVMFragmentDelegate<T : BaseViewModel>(
    private val clazz: KClass<T>,
    private val vmFactory: () -> ViewModelProvider.Factory
) : ReadOnlyProperty<Fragment, T> {

    var cache: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return cache?.let {
            it
        } ?: run {
            val activity = thisRef.requireActivity()
            ViewModelProvider(activity, vmFactory.invoke())[clazz.java].also {
                cache = it
            }
        }
    }
}