package com.jeanpaulo.musiclibrary.app

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.jeanpaulo.musiclibrary.app.databinding.ActivityMusicBinding
import com.jeanpaulo.musiclibrary.settings.ui.SettingsActivity
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong
import com.jeanpaulo.musiclibrary.core.service.MusicPlayerEvents
import com.jeanpaulo.musiclibrary.core.service.MusicPlayerReceiver
import com.jeanpaulo.musiclibrary.music.ui.view.bottom_sheet.FullPlayerBottomSheet
import com.jeanpaulo.musiclibrary.music.ui.view.bottom_sheet.MiniPlayerBottomSheet


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
        setupBottomsheets()
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

        // Nav controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener { item ->
            fullPlayerDialog.hide()

            when (item.itemId) {
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    false
                }

                else -> {
                    //fullPlayerBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                    onNavDestinationSelected(item, navController)
                    true
                }
            }
        }
    }

    //private lateinit var fullPlayerBottomSheet: BottomSheetBehavior<CardView>


    private lateinit var fullPlayerDialog: FullPlayerBottomSheet
    private lateinit var miniPlayerBottomSheet: MiniPlayerBottomSheet
    fun setupBottomsheets() {
        setupMiniPlayer()
        setupFullPlayer()
    }


    fun setupFullPlayer() {
        fullPlayerDialog = FullPlayerBottomSheet.newInstance(
            binding = binding.fullPlayer,
            listener = object : FullPlayerBottomSheet.FullPlayerBottomSheetListener {
                override fun onDismiss() {
                    miniPlayerBottomSheet.expand()
                }

            }
        )
    }

    fun setupMiniPlayer() {
        miniPlayerBottomSheet = MiniPlayerBottomSheet.newInstance(
            binding = binding.miniPlayer,
            listener = object : MiniPlayerBottomSheet.MiniPlayerBottomSheetListener {
                override fun onPlayerPressed() {
                    fullPlayerDialog.expand()
                }
            })
    }

    private val musicPlayerReceiver: MusicPlayerReceiver =
        MusicPlayerReceiver(object : MusicPlayerEvents() {

            override fun onPlaySong(song: MusicPlayerSong) {
                miniPlayerBottomSheet.expand()
                fullPlayerDialog.hide()

                miniPlayerBottomSheet.updatePlayer(song)
                fullPlayerDialog.updatePlayer(song)
            }

            override fun onPlay() {
                miniPlayerBottomSheet.isPlaying()
                fullPlayerDialog.isPlaying()
            }

            override fun onPause() {
                miniPlayerBottomSheet.isPlaying(playing = false)
                fullPlayerDialog.isPlaying(playing = false)
            }

            override fun onStop() {
                miniPlayerBottomSheet.isPlaying(playing = false)
                fullPlayerDialog.isPlaying(playing = false)
            }

            override fun onNext() {
                TODO("Not yet implemented")
            }

            override fun onPrevious() {
                TODO("Not yet implemented")
            }

        })

    private fun setupMusicService() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            musicPlayerReceiver,
            IntentFilter(MusicPlayerReceiver.ACTION),
        )
    }

    override fun onResume() {
        super.onResume()
        setupMusicService()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(musicPlayerReceiver)
        super.onPause()
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
}