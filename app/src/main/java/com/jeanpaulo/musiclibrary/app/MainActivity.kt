package com.jeanpaulo.musiclibrary.app

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.jeanpaulo.musiclibrary.app.databinding.ActivityMusicBinding
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.core.domain.model.Song
import com.jeanpaulo.musiclibrary.player.mp.MPEvents
import com.jeanpaulo.musiclibrary.player.mp.MPReceiver
import com.jeanpaulo.musiclibrary.player.presentation.FullPlayerBottomSheet
import com.jeanpaulo.musiclibrary.player.presentation.MiniPlayerBottomSheet
import com.jeanpaulo.musiclibrary.settings.ui.SettingsActivity


class MainActivity : BaseMvvmActivity() {

    private var _binding: ActivityMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMusicBinding.inflate(layoutInflater).apply {
            _binding = this
        }.root)

        setupNavigation()
        setupPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupNavigation() {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = if (fragment is NavHostFragment) fragment.navController else return

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener { item ->
            fullPlayerDialog?.hide()

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

        binding.navView.setOnItemReselectedListener { menu ->
            findNavController(R.id.nav_host_fragment).clearBackStack(menu.itemId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() ||
                super.onSupportNavigateUp()
    }

    // Players

    private var fullPlayerDialog: FullPlayerBottomSheet? = null
    private var miniPlayerBottomSheet: MiniPlayerBottomSheet? = null

    private fun setupPlayers() {
        setupMiniPlayer()
        setupFullPlayer()
    }

    private fun setupFullPlayer() {
        fullPlayerDialog = FullPlayerBottomSheet.newInstance(
            binding = binding.fullPlayer,
            listener = object : FullPlayerBottomSheet.FullPlayerBottomSheetListener {
                override fun onDismiss() {
                    miniPlayerBottomSheet?.expand()
                }

            }
        )
    }

    private fun setupMiniPlayer() {
        miniPlayerBottomSheet = MiniPlayerBottomSheet.newInstance(
            binding = binding.miniPlayer,
            listener = object : MiniPlayerBottomSheet.MiniPlayerBottomSheetListener {
                override fun onPlayerPressed() {
                    fullPlayerDialog?.expand()
                }
            })
    }

    // RECEIVER

    override fun onResume() {
        super.onResume()
        setupMusicService()
    }

    private fun setupMusicService() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            musicPlayerReceiver,
            IntentFilter(MPReceiver.ACTION),
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(musicPlayerReceiver)
        super.onPause()
    }

    private val musicPlayerReceiver: MPReceiver<Song> =
        MPReceiver({
            Song.fromMPSong(it)
        }, object : MPEvents<Song>() {

            override fun onPlay() {
                miniPlayerBottomSheet?.isPlaying()
                fullPlayerDialog?.isPlaying()
            }

            override fun onPlaySong(currentSong: Song, hasNext: Boolean, hasPrevious: Boolean) {
                miniPlayerBottomSheet?.expand()

                val mpSong = currentSong.toMPSong()

                miniPlayerBottomSheet?.updatePlayer(mpSong)
                fullPlayerDialog?.updatePlayer(mpSong, hasNext, hasPrevious)
            }

            override fun onPause() {
                miniPlayerBottomSheet?.isPlaying(playing = false)
                fullPlayerDialog?.isPlaying(playing = false)
            }

            override fun onStop() {
                miniPlayerBottomSheet?.isPlaying(playing = false)
                fullPlayerDialog?.isPlaying(playing = false)
            }

            override fun onUpdateCounter(counter: Long) {
                fullPlayerDialog?.updateCounter(counter.toInt())
            }
        })
}