package com.jeanpaulo.buscador_itunes.view

interface FragmentListener {

    //FAB
    fun setFabListener(listener: () -> Unit)
    fun setFabDrawableRes(int: Int)
    fun setFabVisibility(visible: Boolean)

    //Toolbar
    fun setTitle(title: String?)

}