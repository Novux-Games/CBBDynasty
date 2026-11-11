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
import dev.novux.dynasty.cbb.data.recruiting.RecruitingInterestSpendRepository
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

    lateinit var nilBudgetRepository: dev.novux.dynasty.cbb.data.nil.NilBudgetRepository
        private set

    lateinit var nilAnnualPoolRepository: dev.novux.dynasty.cbb.data.nil.NilAnnualPoolRepository
        private set

    lateinit var notificationFeedRepository: dev.novux.dynasty.cbb.data.notification.NotificationFeedRepository
        private set

    lateinit var dynastyAutomationRepository: dev.novux.dynasty.cbb.data.settings.DynastyAutomationRepository
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

    lateinit var statisticsHubRepository: dev.novux.dynasty.cbb.data.statistics.StatisticsHubRepository
        private set

    lateinit var selectionSundayRepository: dev.novux.dynasty.cbb.data.selection.SelectionSundayRepository
        private set

    lateinit var conferenceTournamentRepository: dev.novux.dynasty.cbb.data.conference.ConferenceTournamentRepository
        private set

    lateinit var conferenceTournamentHubRepository: dev.novux.dynasty.cbb.data.conference.ConferenceTournamentHubRepository
        private set

    lateinit var ncbaOpeningRoundRepository: dev.novux.dynasty.cbb.data.brackets.NcbaOpeningRoundRepository
        private set

    lateinit var ncbaMainBracketRepository: dev.novux.dynasty.cbb.data.brackets.NcbaMainBracketRepository
        private set

    lateinit var parallelNationalBracketRepository: dev.novux.dynasty.cbb.data.brackets.ParallelNationalBracketRepository
        private set

    lateinit var marchMayhemHubRepository: dev.novux.dynasty.cbb.data.march.MarchMayhemHubRepository
        private set

    lateinit var coachSecurityRepository: CoachSecurityRepository
        private set

    lateinit var seasonReviewRepository: dev.novux.dynasty.cbb.data.seasonreview.SeasonReviewRepository
        private set

    lateinit var playerStatsRepository: dev.novux.dynasty.cbb.data.player.PlayerStatsRepository
        private set

    lateinit var yearlyAwardsRepository: dev.novux.dynasty.cbb.data.awards.YearlyAwardsRepository
        private set

    lateinit var historyRepository: dev.novux.dynasty.cbb.data.history.HistoryRepository
        private set

    lateinit var coachSkillPointRepository: CoachSkillPointRepository
        private set

    lateinit var coachEmploymentRepository: dev.novux.dynasty.cbb.data.coach.CoachEmploymentRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appSettingsRepository = AppSettingsRepository(applicationContext)
        notificationFeedRepository = dev.novux.dynasty.cbb.data.notification.NotificationFeedRepository(
            applicationContext,
        )
        dynastyAutomationRepository = dev.novux.dynasty.cbb.data.settings.DynastyAutomationRepository(
            applicationContext,
        )
        packRepository = PackRepository(applicationContext)
        balanceRulesetRepository = BalanceRulesetRepository(applicationContext)
        simTuningRepository = SimTuningRepository(applicationContext)
        coachingCarouselRepository = CoachingCarouselRepository(applicationContext)
        val recruitingReservationRepository = RecruitingReservationRepository(applicationContext)
        val recruitingInterestSpendRepository = RecruitingInterestSpendRepository(applicationContext)
        nilBudgetRepository = dev.novux.dynasty.cbb.data.nil.NilBudgetRepository(
            applicationContext,
            recruitingReservationRepository,
        )
        val database = DynastyDatabase.build(applicationContext)
        nilAnnualPoolRepository = dev.novux.dynasty.cbb.data.nil.NilAnnualPoolRepository(
            database,
            packRepository,
            nilBudgetRepository,
        )
        portalCycleRepository = PortalCycleRepository(applicationContext)
        incomingClassSeedRepository = IncomingClassSeedRepository(applicationContext)
        weekCheckpointRepository = WeekCheckpointRepository(database)
        val postgameInjuryQueue = dev.novux.dynasty.cbb.data.injuries.PostgameInjuryQueue()
        dynastyRosterRepository = DynastyRosterRepository(database, postgameInjuryQueue)
        offseasonWeekNineRepository = OffseasonWeekNineRepository(applicationContext)
        recruitingEconomyRepository = RecruitingEconomyRepository(
            applicationContext,
            recruitingReservationRepository,
            dynastyRosterRepository,
            offseasonWeekNineRepository,
            database,
            packRepository,
            recruitingInterestSpendRepository,
        )
        aiRecruitingRepository = AiRecruitingRepository(
            applicationContext,
            nilAnnualPoolRepository,
            dynastyAutomationRepository,
            incomingClassSeedRepository,
            recruitingEconomyRepository,
        )
        signingDayRepository = SigningDayRepository(
            applicationContext,
            recruitingReservationRepository,
            nilBudgetRepository,
            incomingClassSeedRepository,
        )
        disciplineRepository = DisciplineRepository(applicationContext, database, balanceRulesetRepository)
        chemistryRepository = ChemistryRepository(applicationContext, balanceRulesetRepository)
        offseasonWeekTwoRepository = OffseasonWeekTwoRepository(applicationContext, dynastyRosterRepository)
        summerWorkoutRepository = OffseasonSummerWorkoutRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekEightRepository = OffseasonWeekEightRepository(applicationContext, dynastyRosterRepository)
        offseasonWeekTenRepository = OffseasonWeekTenRepository(applicationContext, packRepository, database)
        preseasonProgressRepository = PreseasonProgressRepository(applicationContext)
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
        userCalendarRepository = UserCalendarRepository(
            preseasonScheduleRepository,
            packRepository,
            leagueWeekBatchRepository,
            database,
        )
        preseasonReportRepository = PreseasonReportRepository(
            applicationContext,
            packRepository,
            database,
            incomingClassSeedRepository,
        )
        coachSkillPointRepository = CoachSkillPointRepository(applicationContext)
        playerStatsRepository = dev.novux.dynasty.cbb.data.player.PlayerStatsRepository(applicationContext)
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
            coachingCarouselRepository,
            nilAnnualPoolRepository,
            dynastyAutomationRepository,
            appSettingsRepository,
            playerStatsRepository,
            incomingClassSeedRepository,
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
            offseasonWeekNineRepository,
        )
        rankingsHubRepository = dev.novux.dynasty.cbb.data.rankings.RankingsHubRepository(
            packRepository,
            leagueWeekBatchRepository,
            preseasonProgressRepository,
        )
        statisticsHubRepository = dev.novux.dynasty.cbb.data.statistics.StatisticsHubRepository(
            packRepository,
            leagueWeekBatchRepository,
            dynastyRosterRepository,
            playerStatsRepository,
            database,
        )
        selectionSundayRepository = dev.novux.dynasty.cbb.data.selection.SelectionSundayRepository(
            packRepository,
            leagueWeekBatchRepository,
            balanceRulesetRepository,
        )
        conferenceTournamentRepository = dev.novux.dynasty.cbb.data.conference.ConferenceTournamentRepository(
            packRepository,
            leagueWeekBatchRepository,
        )
        conferenceTournamentHubRepository = dev.novux.dynasty.cbb.data.conference.ConferenceTournamentHubRepository(
            packRepository,
            leagueWeekBatchRepository,
            database,
        )
        ncbaOpeningRoundRepository = dev.novux.dynasty.cbb.data.brackets.NcbaOpeningRoundRepository(
            selectionSundayRepository,
            leagueWeekBatchRepository,
        )
        ncbaMainBracketRepository = dev.novux.dynasty.cbb.data.brackets.NcbaMainBracketRepository(
            packRepository,
            leagueWeekBatchRepository,
        )
        parallelNationalBracketRepository = dev.novux.dynasty.cbb.data.brackets.ParallelNationalBracketRepository(
            leagueWeekBatchRepository,
        )
        marchMayhemHubRepository = dev.novux.dynasty.cbb.data.march.MarchMayhemHubRepository(
            packRepository,
            leagueWeekBatchRepository,
            database,
        )
        coachSecurityRepository = CoachSecurityRepository(applicationContext, database)
        coachEmploymentRepository = dev.novux.dynasty.cbb.data.coach.CoachEmploymentRepository(
            applicationContext,
            database,
            packRepository,
        )
        historyRepository = dev.novux.dynasty.cbb.data.history.HistoryRepository(
            applicationContext,
            database,
            packRepository,
            leagueWeekBatchRepository,
            dynastyRosterRepository,
            balanceRulesetRepository,
            newsFeedRepository,
        )
        yearlyAwardsRepository = dev.novux.dynasty.cbb.data.awards.YearlyAwardsRepository(
            applicationContext,
            database,
            packRepository,
            leagueWeekBatchRepository,
            dynastyRosterRepository,
            newsFeedRepository,
            balanceRulesetRepository,
            historyRepository,
        )
        seasonReviewRepository = dev.novux.dynasty.cbb.data.seasonreview.SeasonReviewRepository(
            database,
            packRepository,
            leagueWeekBatchRepository,
            recruitingEconomyRepository,
            offseasonWeekNineRepository,
            coachSecurityRepository,
            yearlyAwardsRepository,
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
