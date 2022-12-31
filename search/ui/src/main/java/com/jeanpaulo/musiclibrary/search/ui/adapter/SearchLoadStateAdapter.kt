package com.jeanpaulo.musiclibrary.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.search.ui.databinding.ItemListFooterBinding

class SearchLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            ItemListFooterBinding.inflate(
                LayoutInflater
                    .from(parent.context),
                parent,
                false
            )

        return LoadStateViewHolder(binding)
    }

    inner class LoadStateViewHolder(private val binding: ItemListFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState !is LoadState.Loading
                txtError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}