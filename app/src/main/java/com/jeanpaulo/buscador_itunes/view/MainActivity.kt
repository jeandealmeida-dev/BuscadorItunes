package com.jeanpaulo.buscador_itunes.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model.NetworkState
import com.jeanpaulo.buscador_itunes.view.list.MusicListAdapter
import com.jeanpaulo.buscador_itunes.view.list.MusicListViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(){

    private lateinit var viewModel: MusicListViewModel
    private lateinit var musicListAdapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        viewModel = ViewModelProvider(this)
            .get(MusicListViewModel::class.java)

        initAdapter()
        initState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initAdapter() {
        musicListAdapter = MusicListAdapter { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = musicListAdapter
        viewModel.newsList.observe(this, Observer {
            musicListAdapter.submitList(it)
        })
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                musicListAdapter.setState(state ?: NetworkState.DONE)
            }
        })
    }

}
