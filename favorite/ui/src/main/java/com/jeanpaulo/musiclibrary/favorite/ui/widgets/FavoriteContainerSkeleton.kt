package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import android.view.View
import com.jeanpaulo.musiclibrary.favorite.ui.R

class FavoriteContainerSkeleton(private var view: View) {

    private var skeleton: SkeletonScreen? = null

    fun showSkeletons(){
        skeleton = Skeleton.bind(view).load(R.layout.favorite_container_skeleton).show()
    }

    fun hideSkeletons(){
        skeleton?.hide()
    }

}