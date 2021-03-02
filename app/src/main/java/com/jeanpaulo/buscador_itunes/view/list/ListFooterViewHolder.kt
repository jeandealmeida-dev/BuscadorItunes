package com.jeanpaulo.buscador_itunes.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model.NetworkState
import kotlinx.android.synthetic.main.item_list_footer.view.*

class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: NetworkState?) {
        itemView.progress_bar.visibility = if (status == NetworkState.LOADING) VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == NetworkState.ERROR) VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return ListFooterViewHolder(
                view
            )
        }
    }
}