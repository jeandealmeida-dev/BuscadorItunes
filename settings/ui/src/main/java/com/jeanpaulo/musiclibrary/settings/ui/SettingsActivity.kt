package com.jeanpaulo.musiclibrary.settings.ui

import android.os.Bundle
import android.view.MenuItem
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.settings.ui.databinding.ActivitySettingsBinding

class SettingsActivity : BaseMvvmActivity() {

    private val viewModel by appViewModel<SettingsViewModel>()
    private var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .commit()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.menu_settings)
        }
    }
}