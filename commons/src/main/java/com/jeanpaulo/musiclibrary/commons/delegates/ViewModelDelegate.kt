package com.jeanpaulo.musiclibrary.commons.delegates

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.DaggerViewModelFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewModelDelegate<T : BaseViewModel>(
    private val clazz: KClass<T>,
    private val activity: FragmentActivity,
    private val vmFactory: () -> DaggerViewModelFactory
) : ReadWriteProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        return ViewModelProvider(activity, vmFactory.invoke())[clazz.java]
    }

    override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) {}
}
