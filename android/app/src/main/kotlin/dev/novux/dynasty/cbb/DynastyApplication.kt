package dev.novux.dynasty.cbb

import android.app.Application
import android.content.Context
import dev.novux.dynasty.cbb.data.carousel.CoachingCarouselRepository
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.data.sim.SimTuningRepository
import dev.novux.dynasty.cbb.data.calendar.WeekCheckpointRepository
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

    lateinit var dynastyRosterRepository: DynastyRosterRepository
        private set

    lateinit var weekCheckpointRepository: WeekCheckpointRepository
        private set

    lateinit var packRepository: PackRepository
        private set

    lateinit var balanceRulesetRepository: BalanceRulesetRepository
        private set

    lateinit var simTuningRepository: SimTuningRepository
        private set

    lateinit var coachingCarouselRepository: CoachingCarouselRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appSettingsRepository = AppSettingsRepository(applicationContext)
        packRepository = PackRepository(applicationContext)
        balanceRulesetRepository = BalanceRulesetRepository(applicationContext)
        simTuningRepository = SimTuningRepository(applicationContext)
        coachingCarouselRepository = CoachingCarouselRepository(applicationContext)
        val database = DynastyDatabase.build(applicationContext)
        weekCheckpointRepository = WeekCheckpointRepository(database)
        dynastyRosterRepository = DynastyRosterRepository(database)
        saveSlotRepository = SaveSlotRepository(
            database,
            weekCheckpointRepository,
            packRepository,
            balanceRulesetRepository,
            simTuningRepository,
        )
        applicationScope.launch {
            saveSlotRepository.ensureInitialized()
            packRepository.ensureEmbeddedLoaded()
            balanceRulesetRepository.ensureShippedDefaultLoaded()
            simTuningRepository.ensureShippedDefaultLoaded()
        }
    }
}

fun Context.requireDynastyApplication(): DynastyApplication =
    applicationContext as DynastyApplication
