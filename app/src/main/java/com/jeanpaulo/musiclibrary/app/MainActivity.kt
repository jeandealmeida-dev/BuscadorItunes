package com.jeanpaulo.musiclibrary.app

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.jeanpaulo.musiclibrary.app.databinding.ActivityMusicBinding
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.core.domain.model.Song
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.core.music_player.MPEvents
import com.jeanpaulo.musiclibrary.core.music_player.MPReceiver
import com.jeanpaulo.musiclibrary.music.ui.view.MusicDetailActivity
import com.jeanpaulo.musiclibrary.music.ui.view.bottom_sheet.FullPlayerBottomSheet
import com.jeanpaulo.musiclibrary.music.ui.view.bottom_sheet.MiniPlayerBottomSheet
import com.jeanpaulo.musiclibrary.settings.ui.SettingsActivity


class MainActivity : BaseMvvmActivity() {

    private val vm by appViewModel<MainViewModel>()

    private lateinit var binding: ActivityMusicBinding

    private lateinit var fullPlayerDialog: FullPlayerBottomSheet
    private lateinit var miniPlayerBottomSheet: MiniPlayerBottomSheet

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
                MainFragmentEnum.FavoritesFragment -> {}
                MainFragmentEnum.PlaylistFragment -> {}
                MainFragmentEnum.SearchFragment -> {}
            }
        }
        vm.state.observe(this) { state ->
            when (state) {
                is MusicActivityState.OpenMusicDetail ->
                    startMusicDetailActivity(state.music)

                else -> {}
            }
        }
    }

    fun startMusicDetailActivity(music: SimpleMusicDetailUIModel) {
        ActivityCompat.startActivity(
            this,
            MusicDetailActivity.newInstance(this, music, false),
            Bundle.EMPTY
        )
    }

    fun setupWidgets() {

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener { item ->
            fullPlayerDialog.hide()

            when (item.itemId) {
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    return@setOnItemSelectedListener true
                }

                else -> {
                    onNavDestinationSelected(item, navController)
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

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

    private val musicPlayerReceiver: MPReceiver<Song> =
        MPReceiver({
            Song.fromMPSong(it)
        }, object : MPEvents<Song>() {

            override fun onPlay() {
                miniPlayerBottomSheet.isPlaying()
                fullPlayerDialog.isPlaying()
            }

            override fun onPlaySong(currentSong: Song, hasNext: Boolean, hasPrevious: Boolean) {
                miniPlayerBottomSheet.expand()

                miniPlayerBottomSheet.updatePlayer(currentSong)
                fullPlayerDialog.updatePlayer(currentSong, hasNext, hasPrevious)
            }

            override fun onPause() {
                miniPlayerBottomSheet.isPlaying(playing = false)
                fullPlayerDialog.isPlaying(playing = false)
            }

            override fun onStop() {
                miniPlayerBottomSheet.isPlaying(playing = false)
                fullPlayerDialog.isPlaying(playing = false)
            }

            override fun onUpdateCounter(counter: Long) {
                fullPlayerDialog.updateCounter(counter.toInt())
            }
        })

    private fun setupMusicService() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            musicPlayerReceiver,
            IntentFilter(MPReceiver.ACTION),
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
        return findNavController(R.id.nav_host_fragment).navigateUp() ||
                super.onSupportNavigateUp()
    }
}