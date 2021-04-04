package com.jeanpaulo.buscador_itunes.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model._Track
import kotlinx.android.synthetic.main.item_track.view.*

class TrackListAdapter(
    private val tracks: List<_Track>,
    private val listener: (_Track) -> Unit
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        )

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bind(tracks[position], listener)


    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(track: _Track, listener: (_Track) -> Unit) = with(itemView) {
            txt_track_name.text = track.name
            setOnClickListener { listener(track) }
        }
    }
}
