package dev.novux.dynasty.cbb

import android.app.Application
import android.content.Context
import dev.novux.dynasty.cbb.data.settings.AppSettingsRepository

class DynastyApplication : Application() {
    lateinit var appSettingsRepository: AppSettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appSettingsRepository = AppSettingsRepository(applicationContext)
    }
}

fun Context.requireDynastyApplication(): DynastyApplication =
    applicationContext as DynastyApplication
