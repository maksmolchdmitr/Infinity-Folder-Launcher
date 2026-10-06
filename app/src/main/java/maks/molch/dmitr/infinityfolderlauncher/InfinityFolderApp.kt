package maks.molch.dmitr.infinityfolderlauncher

import android.app.Application
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FaviconCache
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderBackgroundStore
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.dao.StepsDao

class InfinityFolderApp : Application() {
    lateinit var applicationDao: ApplicationDao
        private set
    lateinit var folderDao: FolderDao
        private set
    lateinit var onboardingDao: OnboardingDao
        private set
    lateinit var settingsDao: SettingsDao
        private set
    lateinit var faviconCache: FaviconCache
        private set
    lateinit var stepsDao: StepsDao
        private set
    lateinit var folderBackgroundStore: FolderBackgroundStore
        private set

    override fun onCreate() {
        super.onCreate()
        folderBackgroundStore = FolderBackgroundStore(this)
        applicationDao = ApplicationDao(this).also { it.start() }
        folderDao = FolderDao(this, folderBackgroundStore)
        onboardingDao = OnboardingDao(this)
        settingsDao = SettingsDao(this)
        faviconCache = FaviconCache(this)
        stepsDao = StepsDao(this)
    }
}
