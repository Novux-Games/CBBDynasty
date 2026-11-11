package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.core.balance.BalanceMedicalRedshirtRules
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.advance.SimWeekConfirmation
import dev.novux.dynasty.cbb.core.coach.employment.CoachEmploymentStatus
import dev.novux.dynasty.cbb.core.dashboard.DashboardAdvanceGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId
import dev.novux.dynasty.cbb.core.dashboard.InSeasonWeekGames
import dev.novux.dynasty.cbb.core.notification.NotificationFeedItem
import dev.novux.dynasty.cbb.core.notification.NotificationSnapshotInput
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubTab
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.ui.advance.SimWeekAdvanceDialog
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.discipline.PendingDisciplineIncident
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.injuries.MedicalRedshirtPolicy
import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.security.CoachSecuritySnapshot
import dev.novux.dynasty.cbb.core.coach.staff.SchoolStaffRoster
import dev.novux.dynasty.cbb.core.preseason.FirstPlayableGame
import dev.novux.dynasty.cbb.core.preview.GamePreviewEngine
import dev.novux.dynasty.cbb.core.preview.GamePreviewRequest
import dev.novux.dynasty.cbb.core.preview.GamePreviewVenue
import dev.novux.dynasty.cbb.core.preview.HeadToHeadHistory
import dev.novux.dynasty.cbb.core.preview.TeamPreviewSide
import dev.novux.dynasty.cbb.ui.preview.GamePreviewLaunch
import dev.novux.dynasty.cbb.core.preseason.PreseasonSeasonStartPolicy
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.preseason.SeasonStartFocus
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.school.SchoolHubOverview
import dev.novux.dynasty.cbb.data.coach.CoachSecurityRepository
import dev.novux.dynasty.cbb.data.coach.CoachSkillPointRepository
import dev.novux.dynasty.cbb.data.coach.SchoolStaffRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.data.chemistry.ChemistryRepository
import dev.novux.dynasty.cbb.data.discipline.DisciplineRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.ui.dashboard.DashboardRequiredTasksPanel
import dev.novux.dynasty.cbb.data.school.SchoolHubRepository
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.ui.dashboard.CareerBlockNotificationDialog
import dev.novux.dynasty.cbb.ui.dashboard.DashboardNotificationFeedPanel
import dev.novux.dynasty.cbb.ui.dashboard.DashboardOptionalTasksPanel
import dev.novux.dynasty.cbb.data.coach.CoachEmploymentRepository
import dev.novux.dynasty.cbb.data.notification.NotificationFeedRepository
import dev.novux.dynasty.cbb.ui.player.PlayerCardPreviewSheet
import dev.novux.dynasty.cbb.ui.player.PlayerCardScreen
import dev.novux.dynasty.cbb.ui.player.PlayerInjuryRosterRow
import dev.novux.dynasty.cbb.ui.player.PlayerLoyaltyChips
import dev.novux.dynasty.cbb.ui.postgame.PostgameInjurySheet
import dev.novux.dynasty.cbb.ui.coach.AssistantCoachCardScreen
import dev.novux.dynasty.cbb.ui.coach.CoachPreviewBottomSheet
import dev.novux.dynasty.cbb.ui.coach.CoachPreviewTarget
import dev.novux.dynasty.cbb.ui.coach.CoachCardRoute
import dev.novux.dynasty.cbb.ui.coach.CoachCardTab
import dev.novux.dynasty.cbb.ui.coach.HeadCoachCardScreen
import dev.novux.dynasty.cbb.ui.coach.SchoolStaffTabPanel
import dev.novux.dynasty.cbb.ui.coach.rememberCoachPreviewSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import dev.novux.dynasty.cbb.ui.school.SchoolHubScreen
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellChrome
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellMenuAction
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellTab
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.ui.hubs.DynastyHubMenuCallbacks
import dev.novux.dynasty.cbb.ui.hubs.buildDynastyHubMenu
import dev.novux.dynasty.cbb.ui.hubs.notifyRecruitingCycleClosed
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellCalendarTab
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellNewsTab
import dev.novux.dynasty.cbb.core.news.NewsHeadline
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellRosterTab
import kotlinx.coroutines.launch

private sealed class ShellCoachView {
    data object Hub : ShellCoachView()
    data object Staff : ShellCoachView()
    data class School(val overview: SchoolHubOverview) : ShellCoachView()
    data class HeadCoach(
        val card: HeadCoachCard,
        val route: CoachCardRoute.HeadCoach,
    ) : ShellCoachView()

    data class Assistant(
        val card: AssistantCoachCard,
        val route: CoachCardRoute.Assistant,
    ) : ShellCoachView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynastyShellScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    scheduleRepository: PreseasonScheduleRepository,
    staffRepository: SchoolStaffRepository,
    schoolHubRepository: SchoolHubRepository,
    coachSecurityRepository: CoachSecurityRepository,
    coachSkillPointRepository: CoachSkillPointRepository,
    rosterRepository: DynastyRosterRepository,
    disciplineRepository: DisciplineRepository,
    chemistryRepository: ChemistryRepository,
    userCalendarRepository: UserCalendarRepository,
    newsFeedRepository: NewsFeedRepository,
    notificationFeedRepository: NotificationFeedRepository,
    coachEmploymentRepository: CoachEmploymentRepository,
    dynastyAutomationRepository: dev.novux.dynasty.cbb.data.settings.DynastyAutomationRepository,
    showNotificationFeed: Boolean = true,
    hubMenuCallbacks: DynastyHubMenuCallbacks,
    onOpenDiscipline: (PendingDisciplineIncident) -> Unit,
    onOpenGamePreview: () -> Unit = {},
    onOpenSigningDay: (dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink) -> Unit = {},
    onOpenRecruitingTab: (RecruitingHubTab) -> Unit = {},
    onOpenAutomation: () -> Unit = {},
    onOpenJobCenter: () -> Unit = {},
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    var pendingAdvanceConfirmation by remember { mutableStateOf<SimWeekConfirmation?>(null) }
    var weekAdvanceInProgress by remember { mutableStateOf(false) }
    var dialogAdvanceInProgress by remember { mutableStateOf(false) }
    var focus by remember { mutableStateOf<SeasonStartFocus?>(null) }
    var staffRoster by remember { mutableStateOf<SchoolStaffRoster?>(null) }
    var coachSecurity by remember { mutableStateOf<CoachSecuritySnapshot?>(null) }
    var userSchool by remember { mutableStateOf<SchoolHubOverview?>(null) }
    var coachView by remember { mutableStateOf<ShellCoachView>(ShellCoachView.Hub) }
    var headCoachTabWire by rememberSaveable { mutableStateOf(CoachCardTab.Overview.wire) }
    val headCoachCardRoute = CoachCardRoute.HeadCoach(
        dynastyId = loaded.dynastyId,
        selectedTabWire = headCoachTabWire,
    )
    var assistantCoachRoutes by remember { mutableStateOf(mapOf<String, CoachCardRoute.Assistant>()) }
    var rosterPreview by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var cardPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var fullScreenPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var rosterPlayers by remember { mutableStateOf<List<RosterPlayer>>(emptyList()) }
    val context = LocalContext.current
    var postgameEvents by remember { mutableStateOf<List<GameSimInjuryEvent>>(emptyList()) }
    var dismissedInjuryTasks by remember { mutableStateOf(setOf<DashboardTaskId>()) }
    var disciplinePending by remember { mutableStateOf<PendingDisciplineIncident?>(null) }
    var teamChemistry by remember { mutableStateOf<Int?>(null) }
    var schedulePlan by remember { mutableStateOf<PreseasonSchedulePlan?>(null) }
    var exhibitionCount by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(DynastyShellTab.Dashboard) }
    var fullRoster by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var newsHeadlines by remember { mutableStateOf<List<NewsHeadline>>(emptyList()) }
    var coachSkillPointBank by remember { mutableIntStateOf(0) }
    var coachSkillState by remember {
        mutableStateOf(dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointState())
    }
    var notificationFeed by remember { mutableStateOf<List<NotificationFeedItem>>(emptyList()) }
    var careerModal by remember { mutableStateOf<NotificationFeedItem?>(null) }
    var coachPreviewTarget by remember { mutableStateOf<CoachPreviewTarget?>(null) }
    var coachEmploymentStatus by remember { mutableStateOf(CoachEmploymentStatus.Employed) }
    var recruitingLedger by remember { mutableStateOf<RecruitingCycleLedger?>(null) }
    val coachPreviewSheetState = rememberCoachPreviewSheetState()
    val scope = rememberCoroutineScope()
    val recruitingEconomyRepository = context.requireDynastyApplication().recruitingEconomyRepository
    val medicalRules = remember { BalanceMedicalRedshirtRules() }
    val position = loaded.weekCheckpoint.activePosition
    val dashboardTasks = DashboardTaskGate.inSeason(
        position = position,
        plan = schedulePlan,
        exhibitionCount = exhibitionCount,
        weekSimStaged = loaded.weekCheckpoint.provisionalGames.isNotEmpty(),
        disciplinePending = disciplinePending,
        rosterPlayers = rosterPlayers,
        medicalRules = medicalRules,
        coachSkillPointBank = coachSkillPointBank,
    )

    LaunchedEffect(loaded.dynastyId) {
        coachEmploymentStatus = coachEmploymentRepository.loadState(loaded.dynastyId).status
    }

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex, loaded.weekCheckpoint) {
        recruitingLedger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
    }

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex, loaded.weekCheckpoint, showNotificationFeed) {
        teamChemistry = chemistryRepository.loadState(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        ).teamChemistry
        exhibitionCount = scheduleRepository.loadExhibitionCount(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        )
        schedulePlan = scheduleRepository.loadPlan(loaded.dynastyId, loaded.metadata.seasonIndex)
        focus = PreseasonSeasonStartPolicy.resolve(
            loaded.metadata.seasonIndex,
            exhibitionCount,
            schedulePlan,
        )
        staffRoster = staffRepository.loadStaffRoster(loaded.dynastyId)
        userSchool = schoolHubRepository.loadUserSchoolOverview(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        )
        coachSecurity = coachSecurityRepository.loadForUserDynasty(loaded.dynastyId)
        rosterPlayers = rosterRepository.listPlayers(loaded.dynastyId)
        fullRoster = rosterRepository.listDynastyPlayers(loaded.dynastyId)
        rosterPreview = fullRoster.take(6)
        disciplinePending = disciplineRepository.loadState(loaded.dynastyId, loaded.metadata.seasonIndex).pending
        postgameEvents = rosterRepository.peekPostgameInjuries(loaded.dynastyId)
        newsHeadlines = newsFeedRepository.shallowFeed(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
            loaded.weekCheckpoint.activePosition.weekIndex,
            rosterPlayers,
        )
        val archetype = staffRoster?.headCoach?.archetype
            ?: dev.novux.dynasty.cbb.core.coach.CoachArchetype.Balanced
        val skillState = coachSkillPointRepository.ensureSundayGrant(
            loaded.dynastyId,
            loaded.weekCheckpoint.activePosition,
            archetype,
        )
        coachSkillState = skillState
        coachSkillPointBank = skillState.unspentBank
        if (showNotificationFeed) {
            val weekGames = InSeasonWeekGames.forPosition(
                loaded.weekCheckpoint.activePosition,
                schedulePlan,
                exhibitionCount,
            )
            val gameLabel = weekGames.firstOrNull()?.let { game ->
                val prep = if (game.homeGame) "vs" else "at"
                "Week ${loaded.weekCheckpoint.activePosition.weekIndex} — $prep ${game.opponentDisplayName}"
            }
            val injuryLines = rosterPlayers.filter { it.injuryActive }.map { player ->
                "${player.displayName} is injured."
            }
            val employment = coachEmploymentRepository.loadState(loaded.dynastyId)
            val snapshot = NotificationSnapshotInput(
                asOfMs = System.currentTimeMillis(),
                newInjurySummaries = injuryLines,
                gameThisWeekLabel = gameLabel,
                coachSkillPointsBank = skillState.unspentBank,
                careerFired = employment.status == CoachEmploymentStatus.Unemployed,
            )
            notificationFeed = notificationFeedRepository.refreshFromSnapshot(loaded.dynastyId, snapshot)
            careerModal = notificationFeedRepository.pendingCareerModal(loaded.dynastyId)
        } else {
            notificationFeed = emptyList()
            careerModal = null
        }
    }

    PlayerCardPreviewSheet(
        player = cardPlayer,
        onDismiss = { cardPlayer = null },
        onViewMore = { player ->
            cardPlayer = null
            fullScreenPlayer = player
        },
    )

    fullScreenPlayer?.let { player ->
        PlayerCardScreen(
            player = player,
            schoolDisplayName = loaded.summary.schoolDisplayName.orEmpty(),
            seasonRecordLabel = staffRoster?.headCoach?.seasonRecordLabel ?: "0-0",
            seasonIndex = loaded.metadata.seasonIndex,
            playerStatsRepository = context.requireDynastyApplication().playerStatsRepository,
            allowEdit = true,
            onBack = { fullScreenPlayer = null },
        )
        return
    }
    PostgameInjurySheet(
        events = postgameEvents,
        players = rosterPlayers,
        medicalEligible = { playerId ->
            val player = rosterPlayers.firstOrNull { it.playerId == playerId } ?: return@PostgameInjurySheet false
            MedicalRedshirtPolicy.evaluate(
                player,
                loaded.weekCheckpoint.activePosition.weekIndex,
                medicalRules,
            ).eligible
        },
        onDismiss = {
            scope.launch {
                if (postgameEvents.isNotEmpty()) {
                    val names = rosterPlayers.associate { it.playerId to it.displayName }
                    newsFeedRepository.appendUserInjuries(
                        loaded.dynastyId,
                        loaded.metadata.seasonIndex,
                        loaded.weekCheckpoint.activePosition.weekIndex,
                        names,
                        postgameEvents,
                    )
                    newsHeadlines = newsFeedRepository.shallowFeed(
                        loaded.dynastyId,
                        loaded.metadata.seasonIndex,
                        loaded.weekCheckpoint.activePosition.weekIndex,
                        rosterPlayers,
                    )
                }
            }
            rosterRepository.clearPostgameInjuries(loaded.dynastyId)
            postgameEvents = emptyList()
        },
        onApplyMedicalRedshirt = { playerId ->
            scope.launch {
                rosterPlayers = rosterRepository.applyMedicalRedshirt(loaded.dynastyId, playerId)
                rosterRepository.clearPostgameInjuries(loaded.dynastyId)
                postgameEvents = emptyList()
                fullRoster = rosterRepository.listDynastyPlayers(loaded.dynastyId)
                rosterPreview = fullRoster.take(6)
            }
        },
    )

    val openHeadCoachFull: (HeadCoachCard) -> Unit = { card ->
        coachView = ShellCoachView.HeadCoach(card, headCoachCardRoute)
    }
    val assistantRouteFor: (AssistantCoachCard) -> CoachCardRoute.Assistant = { card ->
        assistantCoachRoutes[card.staffId]
            ?: CoachCardRoute.Assistant(loaded.dynastyId, card.staffId)
    }
    val openAssistantFull: (AssistantCoachCard) -> Unit = { card ->
        coachView = ShellCoachView.Assistant(card, assistantRouteFor(card))
    }
    val openHeadCoachPreview: (HeadCoachCard) -> Unit = { card ->
        coachPreviewTarget = CoachPreviewTarget.HeadCoach(card)
    }
    val openAssistantPreview: (AssistantCoachCard) -> Unit = { card ->
        coachPreviewTarget = CoachPreviewTarget.Assistant(card)
    }

    LaunchedEffect(coachPreviewTarget) {
        if (coachPreviewTarget != null) {
            coachPreviewSheetState.show()
        }
    }

    careerModal?.let { modal ->
        CareerBlockNotificationDialog(
            item = modal,
            onAcknowledge = {
                scope.launch {
                    notificationFeedRepository.acknowledgeCareerModal(loaded.dynastyId, modal.eventKey)
                    careerModal = notificationFeedRepository.pendingCareerModal(loaded.dynastyId)
                }
            },
            onOpenDestination = {
                when (modal.kind) {
                    dev.novux.dynasty.cbb.core.notification.NotificationEventKind.CareerFired ->
                        hubMenuCallbacks.onExitHome()
                    else -> staffRoster?.headCoach?.let { openHeadCoachPreview(it) }
                }
            },
        )
    }

    CoachPreviewBottomSheet(
        target = coachPreviewTarget,
        userCoachSecurity = coachSecurity,
        sheetState = coachPreviewSheetState,
        onDismiss = { coachPreviewTarget = null },
        onViewMore = {
            when (val target = coachPreviewTarget) {
                is CoachPreviewTarget.HeadCoach -> openHeadCoachFull(target.card)
                is CoachPreviewTarget.Assistant -> openAssistantFull(target.card)
                null -> Unit
            }
        },
    )

    when (val view = coachView) {
        is ShellCoachView.School -> {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedButton(
                    onClick = { coachView = ShellCoachView.Hub },
                    modifier = Modifier.padding(16.dp),
                ) { Text(stringResource(R.string.dynasty_shell_back)) }
                SchoolHubScreen(
                    overview = view.overview,
                    teamChemistry = if (view.overview.isUserSchool) teamChemistry else null,
                    staffSection = if (view.overview.isUserSchool && staffRoster != null) {
                        {
                            SchoolStaffTabPanel(
                                roster = staffRoster!!,
                                adTrust = coachSecurity,
                                onHeadCoachClick = { openHeadCoachPreview(staffRoster!!.headCoach) },
                                onAssistantClick = { id ->
                                    staffRoster!!.assistants.firstOrNull { it.staffId == id }?.let {
                                        openAssistantPreview(it)
                                    }
                                },
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            return
        }
        is ShellCoachView.HeadCoach -> {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedButton(
                    onClick = { coachView = ShellCoachView.Staff },
                    modifier = Modifier.padding(16.dp),
                ) { Text(stringResource(R.string.coach_card_back_staff)) }
                HeadCoachCardScreen(
                    card = view.card,
                    userCoachSecurity = coachSecurity,
                    skillPointBank = coachSkillPointBank,
                    skillState = coachSkillState,
                    programPrestige = userSchool?.programPrestige ?: 50,
                    seasonIndex = loaded.metadata.seasonIndex,
                    selectedTab = view.route.selectedTab,
                    onSelectedTabChange = { tab ->
                        headCoachTabWire = tab.wire
                        coachView = ShellCoachView.HeadCoach(
                            view.card,
                            headCoachCardRoute.withTab(tab),
                        )
                    },
                    onOpenStaffTab = { coachView = ShellCoachView.Staff },
                    employmentStatus = coachEmploymentStatus,
                    onVoluntaryResign = {
                        scope.launch {
                            coachEmploymentRepository.enterUnemployed(
                                loaded.dynastyId,
                                loaded.metadata.seasonIndex,
                            )
                            coachEmploymentStatus = CoachEmploymentStatus.Unemployed
                            onOpenJobCenter()
                        }
                    },
                    onOpenJobCenter = onOpenJobCenter,
                    displayRatings = dev.novux.dynasty.cbb.core.coach.skill.HeadCoachSkillRatings.effective(
                        view.card.ratings,
                        coachSkillState,
                    ),
                    onSpendSkill = { branch ->
                        scope.launch {
                            val next = coachSkillPointRepository.spendOnBranch(loaded.dynastyId, branch)
                            coachSkillState = next
                            coachSkillPointBank = next.unspentBank
                        }
                    },
                    onAutoSpendChanged = { enabled ->
                        scope.launch {
                            val next = coachSkillPointRepository.setAutoSpend(
                                loaded.dynastyId,
                                enabled,
                                view.card.archetype,
                            )
                            val auto = dynastyAutomationRepository.load(loaded.dynastyId)
                            dynastyAutomationRepository.save(
                                loaded.dynastyId,
                                auto.copy(coachAutoSpend = enabled),
                            )
                            coachSkillState = next
                            coachSkillPointBank = next.unspentBank
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            return
        }
        is ShellCoachView.Assistant -> {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedButton(
                    onClick = { coachView = ShellCoachView.Staff },
                    modifier = Modifier.padding(16.dp),
                ) { Text(stringResource(R.string.coach_card_back_staff)) }
                AssistantCoachCardScreen(card = view.card, modifier = Modifier.weight(1f))
            }
            return
        }
        ShellCoachView.Staff -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp, vertical = 24.dp),
            ) {
                OutlinedButton(onClick = { coachView = ShellCoachView.Hub }) {
                    Text(stringResource(R.string.dynasty_shell_back))
                }
                staffRoster?.let { roster ->
                    SchoolStaffTabPanel(
                        roster = roster,
                        adTrust = coachSecurity,
                        onHeadCoachClick = { openHeadCoachPreview(roster.headCoach) },
                        onAssistantClick = { id ->
                            roster.assistants.firstOrNull { it.staffId == id }?.let {
                                openAssistantPreview(it)
                            }
                        },
                    )
                }
            }
            return
        }
        ShellCoachView.Hub -> Unit
    }

    val schoolName = loaded.summary.schoolDisplayName.orEmpty()
    val phaseLabel = CalendarWeekLabel.display(position)
    val recruitingCycleClosed = recruitingLedger?.cycleClosed == true
    val shellMenu = buildDynastyHubMenu(
        position = position,
        callbacks = hubMenuCallbacks.copy(
            onOpenSchool = { userSchool?.let { coachView = ShellCoachView.School(it) } },
        ),
        recruitingCycleClosed = recruitingCycleClosed,
        onRecruitingCycleClosed = { notifyRecruitingCycleClosed(context) },
        leadingExtras = listOf(
            DynastyShellMenuAction(R.string.staff_open_tab) { coachView = ShellCoachView.Staff },
            DynastyShellMenuAction(R.string.shell_menu_automation, onOpenAutomation),
        ),
    )

    DynastyShellChrome(
        schoolDisplayName = schoolName,
        phaseWeekLabel = phaseLabel,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        menuActions = shellMenu,
    ) { padding ->
        when (selectedTab) {
            DynastyShellTab.Roster -> DynastyShellRosterTab(
                players = fullRoster,
                contentPadding = padding,
                onPlayerClick = { cardPlayer = it },
            )
            DynastyShellTab.Calendar -> DynastyShellCalendarTab(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                activePosition = position,
                calendarRepository = userCalendarRepository,
                contentPadding = padding,
            )
            DynastyShellTab.News -> DynastyShellNewsTab(headlines = newsHeadlines, contentPadding = padding)
            DynastyShellTab.Dashboard -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.dynasty_shell_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (rosterPreview.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.dynasty_shell_roster),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    rosterPreview.forEach { player ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { cardPlayer = player },
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                PlayerInjuryRosterRow(player = player.toRosterPlayer())
                                PlayerLoyaltyChips(player)
                            }
                        }
                    }
                }
                if (showNotificationFeed) {
                    DashboardNotificationFeedPanel(
                        feed = notificationFeed,
                        onDeepLink = onOpenRecruitingTab,
                        onStrategyOpen = hubMenuCallbacks.onOpenStrategy,
                        onCoachCardOpen = { staffRoster?.headCoach?.let { openHeadCoachPreview(it) } },
                        onSigningDayOpen = { onOpenSigningDay(DashboardDeepLink.SigningDayRankings) },
                    )
                }
                DashboardRequiredTasksPanel(
                    tasks = dashboardTasks.required,
                    onOpenSigningDay = onOpenSigningDay,
                    onOpenDiscipline = { disciplinePending?.let(onOpenDiscipline) },
                )
                DashboardOptionalTasksPanel(
                    tasks = dashboardTasks.optional,
                    dismissedIds = dismissedInjuryTasks,
                    onDismiss = { dismissedInjuryTasks = dismissedInjuryTasks + it },
                    onDeepLink = onOpenRecruitingTab,
                    onStrategyOpen = hubMenuCallbacks.onOpenStrategy,
                    onCoachCardOpen = { staffRoster?.headCoach?.let { openHeadCoachPreview(it) } },
                )
                focus?.firstGame?.let { game ->
                    NextGameCard(
                        game = game,
                        onViewOpponentSchool = {
                            val opponentId = game.opponentTeamId
                            if (!opponentId.startsWith("event-")) {
                                scope.launch {
                                    schoolHubRepository.loadOpponentOverview(
                                        loaded.dynastyId,
                                        opponentId,
                                        loaded.metadata.seasonIndex,
                                    )?.let { coachView = ShellCoachView.School(it) }
                                }
                            }
                        },
                        onOpenPreview = {
                            scope.launch {
                                val user = userSchool ?: return@launch
                                val oppOverview = if (!game.opponentTeamId.startsWith("event-")) {
                                    schoolHubRepository.loadOpponentOverview(
                                        loaded.dynastyId,
                                        game.opponentTeamId,
                                        loaded.metadata.seasonIndex,
                                    )
                                } else {
                                    null
                                }
                                val opponent = oppOverview?.let { TeamPreviewSide.fromOverview(it) }
                                    ?: TeamPreviewSide.fromPrestige(
                                        game.opponentTeamId,
                                        game.opponentDisplayName,
                                        58,
                                    )
                                val venue = if (game.homeGame) {
                                    GamePreviewVenue.Home
                                } else {
                                    GamePreviewVenue.Away
                                }
                                val preview = GamePreviewEngine.predict(
                                    GamePreviewRequest(
                                        user = TeamPreviewSide.fromOverview(user),
                                        opponent = opponent,
                                        venue = venue,
                                        headToHead = HeadToHeadHistory.summarize(emptyList()),
                                    ),
                                )
                                val loc = if (game.homeGame) {
                                    context.getString(R.string.dynasty_shell_home)
                                } else {
                                    context.getString(R.string.dynasty_shell_away)
                                }
                                GamePreviewLaunch.matchupLabel = context.getString(
                                    R.string.dynasty_shell_matchup,
                                    game.opponentDisplayName,
                                    loc,
                                )
                                GamePreviewLaunch.userTeamName = user.displayName
                                GamePreviewLaunch.opponentName = game.opponentDisplayName
                                GamePreviewLaunch.venue = venue
                                GamePreviewLaunch.venuePhrase = if (game.homeGame) {
                                    context.getString(R.string.game_preview_venue_phrase_home)
                                } else {
                                    context.getString(R.string.game_preview_venue_phrase_away)
                                }
                                GamePreviewLaunch.result = preview
                                onOpenGamePreview()
                            }
                        },
                    )
                }
                pendingAdvanceConfirmation?.let { confirmation ->
                    SimWeekAdvanceDialog(
                        confirmation = confirmation,
                        isAdvancing = dialogAdvanceInProgress,
                        onConfirm = {
                            scope.launch {
                                dialogAdvanceInProgress = true
                                try {
                                    when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                                        is DynastyLoadResult.Success -> {
                                            loaded = result.loaded
                                            ActiveDynastySession.set(result.loaded)
                                            dismissedInjuryTasks = emptySet()
                                            pendingAdvanceConfirmation = null
                                        }
                                        else -> Unit
                                    }
                                } finally {
                                    dialogAdvanceInProgress = false
                                }
                            }
                        },
                        onDismiss = {
                            if (!dialogAdvanceInProgress) {
                                pendingAdvanceConfirmation = null
                            }
                        },
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            if (DashboardAdvanceGate.blocksWeekAdvance(dashboardTasks.required)) {
                                return@launch
                            }
                            weekAdvanceInProgress = true
                            try {
                                val unresolved = dashboardTasks.optional.count { it.id !in dismissedInjuryTasks }
                                val confirmation = saveSlotRepository.buildSimWeekAdvanceConfirmation(
                                    loaded.slotIndex,
                                    unresolvedOptionalTaskCount = unresolved,
                                )
                                if (confirmation != null && confirmation.needsDialog) {
                                    pendingAdvanceConfirmation = confirmation
                                } else {
                                    when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                                        is DynastyLoadResult.Success -> {
                                            loaded = result.loaded
                                            ActiveDynastySession.set(result.loaded)
                                            dismissedInjuryTasks = emptySet()
                                        }
                                        else -> Unit
                                    }
                                }
                            } finally {
                                weekAdvanceInProgress = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !weekAdvanceInProgress &&
                        !dialogAdvanceInProgress &&
                        !DashboardAdvanceGate.blocksWeekAdvance(dashboardTasks.required),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (weekAdvanceInProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                            )
                        }
                        Text(
                            stringResource(
                                if (weekAdvanceInProgress) {
                                    R.string.dynasty_shell_advance_week_in_progress
                                } else {
                                    R.string.dynasty_shell_advance_week
                                },
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NextGameCard(
    game: FirstPlayableGame,
    onViewOpponentSchool: () -> Unit,
    onOpenPreview: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.dynasty_shell_next_game),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(
                    R.string.dynasty_shell_matchup,
                    game.opponentDisplayName,
                    if (game.homeGame) {
                        stringResource(R.string.dynasty_shell_home)
                    } else {
                        stringResource(R.string.dynasty_shell_away)
                    },
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.dynasty_shell_game_type, game.kind.name),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onViewOpponentSchool) {
                Text(stringResource(R.string.school_hub_view_opponent))
            }
            OutlinedButton(onClick = onOpenPreview, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.dynasty_shell_game_preview))
            }
        }
    }
}
