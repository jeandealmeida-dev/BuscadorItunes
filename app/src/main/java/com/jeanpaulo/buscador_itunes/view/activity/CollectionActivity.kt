package com.jeanpaulo.buscador_itunes.view.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model.Collection
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.Track
import com.jeanpaulo.buscador_itunes.util.CustomCallback
import com.jeanpaulo.buscador_itunes.util.MyMediaPlayer
import com.jeanpaulo.buscador_itunes.view.adapter.TrackListAdapter
import com.jeanpaulo.buscador_itunes.view_model.CollectionViewModel
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.fragment_collection.*

class CollectionActivity : AppCompatActivity(), CustomCallback<Collection>{

    private lateinit var viewModel: CollectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val collectionId = intent.getLongExtra("collectionId", 0L)

        viewModel = CollectionViewModel(
            this,
            collectionId = collectionId
        )
        viewModel.getCollection()
        initState()
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (state == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (state == NetworkState.ERROR) View.VISIBLE else View.GONE
            /*if (!viewModel.listIsEmpty()) {
                //trackAdapter.setState(state ?: NetworkState.DONE)
            }*/
        })
    }

    override fun onResult(collection: Collection) {
        txt_collection_name.text = collection.name
        txt_artist_name.text = collection.artistName

        Picasso.get().load(collection.artwork).into(img_collection)

        val mediaPlayer = MyMediaPlayer()
        val adapter =
            TrackListAdapter(
                collection.tracks,
                fun(track: Track) {
                    mediaPlayer.stop()
                    mediaPlayer.play(this, track.previewUrl)
                })

        list_track.adapter = adapter
    }

    override fun onBackPressed() {
        viewModel.onClose()
        super.onBackPressed()
    }

}
