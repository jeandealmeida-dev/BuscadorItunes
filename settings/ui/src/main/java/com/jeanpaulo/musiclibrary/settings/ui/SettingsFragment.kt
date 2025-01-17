package com.jeanpaulo.musiclibrary.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.commons.delegates.ActivityVMFragmentDelegate

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by ActivityVMFragmentDelegate(SettingsViewModel::class) {
        (requireActivity() as BaseMvvmActivity).vmFactory
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupSettings()
    }

    private fun setupSettings() {
        findPreference<ListPreference>(getString(R.string.preference_theme_key))
            ?.setOnPreferenceChangeListener { _, newValue ->
                applyTheme(resources, newValue as String?)
                true
            }

        findPreference<Preference>(getString(R.string.preference_clear_data_key))
            ?.setOnPreferenceClickListener {
                viewModel.clearDatabase()
                true
            }

        findPreference<Preference>(getString(R.string.preference_feedback_key))
            ?.setOnPreferenceClickListener {
                sendEmailAction()
                true
            }
    }

    private fun sendEmailAction() {
        val subject = getString(R.string.settings_feedback_mail_subject)
        val mailTo = getString(R.string.settings_feedback_mail_to)

        startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("mailto:?subject=$subject&to=$mailTo")
            })
    }
}