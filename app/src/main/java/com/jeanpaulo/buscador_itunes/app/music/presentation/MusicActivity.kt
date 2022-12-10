package com.jeanpaulo.buscador_itunes.app.music.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jeanpaulo.buscador_itunes.R
import androidx.core.util.Pair
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import com.jeanpaulo.buscador_itunes.app.music.presentation.viewmodel.MusicActivityState
import com.jeanpaulo.buscador_itunes.app.music.presentation.viewmodel.MusicFragmentEnum
import com.jeanpaulo.buscador_itunes.app.music.presentation.viewmodel.MusicViewModel
import com.jeanpaulo.buscador_itunes.databinding.ActivityMusicBinding
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity.Companion.VIEW_NAME_HEADER_TITLE
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity.Companion.VIEW_NAME_HEADER_IMAGE
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity

class MusicActivity : BaseMvvmActivity() {

    private val vm by appViewModel<MusicViewModel>()

    private lateinit var binding: ActivityMusicBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(vm)

        setupListeners()
        setupWidgets()
    }

    fun setupListeners() {
        vm.actMusicFragment.observe(this) { fragmentEnum ->
            when (fragmentEnum) {
                MusicFragmentEnum.FavoritesFragment -> {
                }
                MusicFragmentEnum.PlaylistFragment -> {

                }
                MusicFragmentEnum.SearchFragment -> {

                }
            }
        }
        vm.state.observe(this) { state ->
            when (state) {
                is MusicActivityState.OpenMusicDetail -> {
                    startMusicDetailActivity(state.view, state.music)
                }
            }
        }
    }

    fun setupWidgets() {
        // Toolbar
        setSupportActionBar(binding.toolbar)

        // Nav controller
        val navController: NavController = findNavController(R.id.nav_host_fragment)

        // AppBarConfigurations
        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_favorite, R.id.nav_playlist, R.id.nav_search
                ), binding.drawerLayout
            )

        // setup components
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupWithNavController(binding.navView, navController)

        // Drawer Layout
        binding.drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
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


    fun startMusicDetailActivity(view: View, music: SimpleMusicDetailUIModel) {
        val intent = MusicDetailActivity.newInstance(
            this,
            music,
        )

        //Animations
        val titleElement = Pair.create<View, String>(
            view.findViewById(R.id.musicName),
            VIEW_NAME_HEADER_TITLE
        )
        val imageElement =
            Pair.create<View, String>(view.findViewById(R.id.artwork), VIEW_NAME_HEADER_IMAGE)

        val activityOptions =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, titleElement, imageElement)
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
    }

//    override fun hideKeyboard() {
//        this.currentFocus?.let { view ->
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }

//    override fun setFabListener(listener: () -> Unit) {
//        binding.fab.setOnClickListener { listener() }
//    }
//
//    override fun setFabDrawableRes(imageResource: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            binding.fab.setImageDrawable(resources.getDrawable(imageResource, theme))
//        } else {
//            binding.fab.setImageDrawable(resources.getDrawable(imageResource))
//        }
//    }
//
//    override fun setFabVisibility(visible: Boolean) {
//        binding.fab.visibility = if (visible) View.VISIBLE else View.GONE
//    }
//
//    override fun setTitle(title: String?) {
//        supportActionBar?.title = title
//    }

    companion object {
        // Keys for navigation
        const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
        const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
        const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

        fun newInstance(context: Context): Intent {
            return Intent(context, MusicActivity::class.java)
        }
    }
}

