package com.jeanpaulo.buscador_itunes.view.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.NetworkState

class MusicListAdapter(private val retry: () -> Unit)
    : PagedListAdapter<Music, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) MusicViewHolder.create(parent) else ListFooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as MusicViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == NetworkState.LOADING || state == NetworkState.ERROR)
    }

    fun setState(state: NetworkState) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}