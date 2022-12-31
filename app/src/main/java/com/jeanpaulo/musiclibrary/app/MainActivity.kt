package com.jeanpaulo.musiclibrary.app

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jeanpaulo.musiclibrary.app.databinding.ActivityMusicBinding
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity

class MainActivity : BaseMvvmActivity() {

    private val vm by appViewModel<MainViewModel>()

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
                MainFragmentEnum.FavoritesFragment -> {
                }
                MainFragmentEnum.PlaylistFragment -> {

                }
                MainFragmentEnum.SearchFragment -> {

                }
            }
        }
        vm.state.observe(this) { state ->
            when (state) {
//                is MusicActivityState.OpenMusicDetail -> {
//                    startMusicDetailActivity(state.view, state.music)
//                }
                else -> {}
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
        NavigationUI.setupWithNavController(binding.navView, navController)

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
}