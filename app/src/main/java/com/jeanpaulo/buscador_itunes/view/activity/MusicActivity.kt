package com.jeanpaulo.buscador_itunes.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main._activity_music.*
import androidx.core.util.Pair

class MusicActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

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

    override fun onResume() {
        super.onResume()
    }

    fun startMusicDetailActivity(view: View, musicId: Long, artworkUrl: String) {
        val intent = Intent(this, MusicDetailActivity::class.java)
        val b = Bundle()
        b.putLong(TRACK_ID_PARAM, musicId) //Your id
        b.putString(ARTWORK_URL_PARAM, artworkUrl) //Your id
        intent.putExtras(b)

        //Animations
        val p1 =Pair.create<View, String>(view.findViewById(R.id.txt_music_name), VIEW_NAME_HEADER_TITLE)
        val p2  = Pair.create<View, String>(view.findViewById(R.id.img_artwork), VIEW_NAME_HEADER_TITLE)

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        //startActivity(intent)
        //overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }

}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
