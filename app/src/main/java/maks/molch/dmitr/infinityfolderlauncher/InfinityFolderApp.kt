package maks.molch.dmitr.infinityfolderlauncher

import android.app.Application
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao

class InfinityFolderApp : Application() {
    lateinit var applicationDao: ApplicationDao
        private set
    lateinit var folderDao: FolderDao
        private set
    lateinit var onboardingDao: OnboardingDao
        private set
    lateinit var settingsDao: SettingsDao
        private set

    override fun onCreate() {
        super.onCreate()
        applicationDao = ApplicationDao(this).also { it.start() }
        folderDao = FolderDao(this)
        onboardingDao = OnboardingDao(this)
        settingsDao = SettingsDao(this)
    }
}
