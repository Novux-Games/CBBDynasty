package dev.novux.dynasty.cbb

import android.app.Application
import android.content.Context
import dev.novux.dynasty.cbb.data.carousel.CoachingCarouselRepository
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.data.sim.SimTuningRepository
import dev.novux.dynasty.cbb.data.calendar.WeekCheckpointRepository
import dev.novux.dynasty.cbb.data.portal.PortalCycleRepository
import dev.novux.dynasty.cbb.data.recruiting.AiRecruitingRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingReservationRepository
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonSummerWorkoutRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekEightRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekNineRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekTenRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekTwoRepository
import dev.novux.dynasty.cbb.data.signingday.SigningDayRepository
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

    lateinit var recruitingEconomyRepository: RecruitingEconomyRepository
        private set

    lateinit var portalCycleRepository: PortalCycleRepository
        private set

    lateinit var aiRecruitingRepository: AiRecruitingRepository
        private set

    lateinit var signingDayRepository: SigningDayRepository
        private set

    lateinit var offseasonWeekTwoRepository: OffseasonWeekTwoRepository
        private set

    lateinit var summerWorkoutRepository: OffseasonSummerWorkoutRepository
        private set

    lateinit var offseasonWeekEightRepository: OffseasonWeekEightRepository
        private set

    lateinit var offseasonWeekNineRepository: OffseasonWeekNineRepository
        private set

    lateinit var offseasonWeekTenRepository: OffseasonWeekTenRepository
        private set

    lateinit var preseasonProgressRepository: PreseasonProgressRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appSettingsRepository = AppSettingsRepository(applicationContext)
        packRepository = PackRepository(applicationContext)
        balanceRulesetRepository = BalanceRulesetRepository(applicationContext)
        simTuningRepository = SimTuningRepository(applicationContext)
        coachingCarouselRepository = CoachingCarouselRepository(applicationContext)
        val recruitingReservationRepository = RecruitingReservationRepository(applicationContext)
        recruitingEconomyRepository = RecruitingEconomyRepository(
            applicationContext,
            recruitingReservationRepository,
        )
        signingDayRepository = SigningDayRepository(applicationContext, recruitingReservationRepository)
        portalCycleRepository = PortalCycleRepository(applicationContext)
        aiRecruitingRepository = AiRecruitingRepository(applicationContext)
        val database = DynastyDatabase.build(applicationContext)
        weekCheckpointRepository = WeekCheckpointRepository(database)
        dynastyRosterRepository = DynastyRosterRepository(database)
        offseasonWeekTwoRepository = OffseasonWeekTwoRepository(applicationContext, dynastyRosterRepository)
        summerWorkoutRepository = OffseasonSummerWorkoutRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekEightRepository = OffseasonWeekEightRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekNineRepository = OffseasonWeekNineRepository(applicationContext)
        offseasonWeekTenRepository = OffseasonWeekTenRepository(applicationContext, packRepository, database)
        preseasonProgressRepository = PreseasonProgressRepository(applicationContext)
        saveSlotRepository = SaveSlotRepository(
            database,
            weekCheckpointRepository,
            packRepository,
            balanceRulesetRepository,
            simTuningRepository,
            recruitingEconomyRepository,
            portalCycleRepository,
            aiRecruitingRepository,
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
