package dev.novux.dynasty.cbb

import android.app.Application
import android.content.Context
import dev.novux.dynasty.cbb.data.carousel.CoachingCarouselRepository
import dev.novux.dynasty.cbb.data.coach.CoachSecurityRepository
import dev.novux.dynasty.cbb.data.coach.CoachSkillPointRepository
import dev.novux.dynasty.cbb.data.coach.SchoolStaffRepository
import dev.novux.dynasty.cbb.data.school.SchoolHubRepository
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.chemistry.ChemistryRepository
import dev.novux.dynasty.cbb.data.discipline.DisciplineRepository
import dev.novux.dynasty.cbb.data.league.LeagueScheduleRepository
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.data.sim.SimTuningRepository
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository
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
import dev.novux.dynasty.cbb.data.preseason.EarlySeasonBracketRepository
import dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonReportRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonSeasonStartRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.data.preseason.TournamentInvitesRepository
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

    lateinit var disciplineRepository: DisciplineRepository
        private set

    lateinit var chemistryRepository: ChemistryRepository
        private set

    lateinit var userCalendarRepository: UserCalendarRepository
        private set

    lateinit var newsFeedRepository: NewsFeedRepository
        private set

    lateinit var leagueScheduleRepository: LeagueScheduleRepository
        private set

    lateinit var leagueWeekBatchRepository: LeagueWeekBatchRepository
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

    lateinit var incomingClassSeedRepository: IncomingClassSeedRepository
        private set

    lateinit var tournamentInvitesRepository: TournamentInvitesRepository
        private set

    lateinit var earlySeasonBracketRepository: EarlySeasonBracketRepository
        private set

    lateinit var preseasonScheduleRepository: PreseasonScheduleRepository
        private set

    lateinit var preseasonReportRepository: PreseasonReportRepository
        private set

    lateinit var preseasonSeasonStartRepository: PreseasonSeasonStartRepository
        private set

    lateinit var schoolStaffRepository: SchoolStaffRepository
        private set

    lateinit var schoolHubRepository: SchoolHubRepository
        private set

    lateinit var rankingsHubRepository: dev.novux.dynasty.cbb.data.rankings.RankingsHubRepository
        private set

    lateinit var selectionSundayRepository: dev.novux.dynasty.cbb.data.selection.SelectionSundayRepository
        private set

    lateinit var coachSecurityRepository: CoachSecurityRepository
        private set

    lateinit var coachSkillPointRepository: CoachSkillPointRepository
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
        val postgameInjuryQueue = dev.novux.dynasty.cbb.data.injuries.PostgameInjuryQueue()
        dynastyRosterRepository = DynastyRosterRepository(database, postgameInjuryQueue)
        disciplineRepository = DisciplineRepository(applicationContext, database, balanceRulesetRepository)
        chemistryRepository = ChemistryRepository(applicationContext, balanceRulesetRepository)
        offseasonWeekTwoRepository = OffseasonWeekTwoRepository(applicationContext, dynastyRosterRepository)
        summerWorkoutRepository = OffseasonSummerWorkoutRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekEightRepository = OffseasonWeekEightRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekNineRepository = OffseasonWeekNineRepository(applicationContext)
        offseasonWeekTenRepository = OffseasonWeekTenRepository(applicationContext, packRepository, database)
        preseasonProgressRepository = PreseasonProgressRepository(applicationContext)
        incomingClassSeedRepository = IncomingClassSeedRepository(applicationContext)
        tournamentInvitesRepository = TournamentInvitesRepository(applicationContext)
        earlySeasonBracketRepository = EarlySeasonBracketRepository(
            applicationContext,
            packRepository,
            tournamentInvitesRepository,
            database,
        )
        leagueScheduleRepository = LeagueScheduleRepository(applicationContext, packRepository)
        preseasonScheduleRepository = PreseasonScheduleRepository(
            applicationContext,
            packRepository,
            database,
            tournamentInvitesRepository,
            leagueScheduleRepository,
        )
        userCalendarRepository = UserCalendarRepository(preseasonScheduleRepository, packRepository, database)
        newsFeedRepository = NewsFeedRepository(applicationContext, incomingClassSeedRepository)
        leagueWeekBatchRepository = LeagueWeekBatchRepository(
            applicationContext,
            leagueScheduleRepository,
            preseasonScheduleRepository,
            earlySeasonBracketRepository,
            newsFeedRepository,
            database,
            packRepository,
            balanceRulesetRepository,
        )
        preseasonReportRepository = PreseasonReportRepository(
            applicationContext,
            packRepository,
            database,
            incomingClassSeedRepository,
        )
        coachSkillPointRepository = CoachSkillPointRepository(applicationContext)
        saveSlotRepository = SaveSlotRepository(
            database,
            weekCheckpointRepository,
            packRepository,
            balanceRulesetRepository,
            simTuningRepository,
            recruitingEconomyRepository,
            portalCycleRepository,
            aiRecruitingRepository,
            dynastyRosterRepository,
            disciplineRepository,
            chemistryRepository,
            leagueWeekBatchRepository,
            preseasonScheduleRepository,
            coachSkillPointRepository,
        )
        preseasonSeasonStartRepository = PreseasonSeasonStartRepository(
            saveSlotRepository,
            preseasonProgressRepository,
            preseasonScheduleRepository,
            leagueScheduleRepository,
            earlySeasonBracketRepository,
        )
        schoolStaffRepository = SchoolStaffRepository(database, packRepository)
        schoolHubRepository = SchoolHubRepository(
            database,
            packRepository,
            preseasonProgressRepository,
            leagueWeekBatchRepository,
            balanceRulesetRepository,
        )
        rankingsHubRepository = dev.novux.dynasty.cbb.data.rankings.RankingsHubRepository(
            packRepository,
            leagueWeekBatchRepository,
            preseasonProgressRepository,
        )
        selectionSundayRepository = dev.novux.dynasty.cbb.data.selection.SelectionSundayRepository(
            packRepository,
            leagueWeekBatchRepository,
            balanceRulesetRepository,
        )
        coachSecurityRepository = CoachSecurityRepository(applicationContext, database)
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
