package com.jeanpaulo.musiclibrary.ds.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.jeanpaulo.musiclibrary.ds.R

class SongListSkeleton(private var recyclerView: RecyclerView) {

    var skeletonScreen: SkeletonScreen? = null
    var count = 5

    fun showSkeletons() {
        skeletonScreen =
            Skeleton.bind(recyclerView)
                .adapter(recyclerView.adapter)
                .count(count)
                .load(R.layout.song_item_skeleton)
                .show()
    }

    fun hideSkeletons() {
        skeletonScreen?.hide()
    }

}