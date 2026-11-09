package dev.novux.dynasty.cbb

import android.app.Application
import android.content.Context
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.data.settings.AppSettingsRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DynastyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var appSettingsRepository: AppSettingsRepository
        private set

    lateinit var saveSlotRepository: SaveSlotRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appSettingsRepository = AppSettingsRepository(applicationContext)
        val database = DynastyDatabase.build(applicationContext)
        saveSlotRepository = SaveSlotRepository(database.saveSlotDao())
        applicationScope.launch {
            saveSlotRepository.ensureInitialized()
        }
    }
}

fun Context.requireDynastyApplication(): DynastyApplication =
    applicationContext as DynastyApplication
