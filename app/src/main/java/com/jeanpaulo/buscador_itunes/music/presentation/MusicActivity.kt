package com.jeanpaulo.buscador_itunes.music.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.jeanpaulo.buscador_itunes.R
import androidx.core.util.Pair
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jeanpaulo.buscador_itunes.favorite.presentation.view.FavoriteFragmentListener
import com.jeanpaulo.buscador_itunes.view.fragment.add_edit_playlist.AddEditPlaylistFragmentListener
import com.jeanpaulo.buscador_itunes.view.fragment.playlist_list.PlaylistFragmentListener
import com.jeanpaulo.buscador_itunes.music._music_search.SearchFragmentListener
import com.jeanpaulo.buscador_itunes.view.music.music_search.music_detail.*
import kotlinx.android.synthetic.main._activity_music.toolbar

class MusicActivity : AppCompatActivity(),
    PlaylistFragmentListener,
    AddEditPlaylistFragmentListener,
    SearchFragmentListener,
    FavoriteFragmentListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        setupNavigationDrawer()
        setSupportActionBar(toolbar)

        val navController: NavController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.search_fragment_dest)
                .setDrawerLayout(drawerLayout)
                .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        fab = findViewById(R.id.fab)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        //Removed super.onCreateContextMenu to work with context on fragment
        //REF: https://stackoverflow.com/questions/20825118/inappropriate-context-menu-within-a-fragment
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.colorPrimaryDark)
            }
    }

    fun startMusicDetailActivity(view: View, musicId: Long, musicName: String, artworkUrl: String) {
        val intent = Intent(this, MusicDetailActivity::class.java)
        val b = Bundle()
        b.putLong(MUSIC_ID_PARAM, musicId) //Your id
        b.putString(MUSIC_NAME_PARAM, musicName)
        b.putString(ARTWORK_URL_PARAM, artworkUrl) //Your id
        intent.putExtras(b)

        //Animations
        val p1 = Pair.create<View, String>(
            view.findViewById(R.id.txt_music_name),
            VIEW_NAME_HEADER_TITLE
        )
        val p2 = Pair.create<View, String>(
            view.findViewById(R.id.img_artwork),
            VIEW_NAME_HEADER_TITLE
        )

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        //startActivity(intent)
        //overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }

    override fun openMusicDetailActivity(
        view: View,
        musicId: Long,
        musicName: String,
        artworkUrl: String
    ) {
        startMusicDetailActivity(view, musicId, musicName, artworkUrl)
    }

    override fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun setFabListener(listener: () -> Unit) {
        fab.setOnClickListener { listener() }
    }

    override fun setFabDrawableRes(imageResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(imageResource, getTheme()))
        } else {
            fab.setImageDrawable(getResources().getDrawable(imageResource))
        }
    }

    override fun setFabVisibility(visible: Boolean) {
        fab.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setTitle(title: String?) {
        supportActionBar?.title = title
    }

}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
