package com.jeanpaulo.musiclibrary.settings.domain

import javax.inject.Inject

interface SettingsRepository {

}

interface SettingsInteractor {
    fun clearData()
}

class SettingsInteractorImpl @Inject constructor(
    val repository: SettingsRepository
) : SettingsInteractor {
    override fun clearData() {
        TODO("Not yet implemented")
    }
}