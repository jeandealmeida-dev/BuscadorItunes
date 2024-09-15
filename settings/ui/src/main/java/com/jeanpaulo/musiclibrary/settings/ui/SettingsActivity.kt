package com.jeanpaulo.musiclibrary.settings.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.settings.ui.databinding.ActivitySettingsBinding

class SettingsActivity : BaseMvvmActivity() {

    private val viewModel by appViewModel<SettingsViewModel>()
    private var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding).root)

        setupToolbar()
        setupFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupFragment() {
        supportFragmentManager.commit {
            replace(R.id.container, SettingsFragment())
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.menu_settings)
        }
    }
}