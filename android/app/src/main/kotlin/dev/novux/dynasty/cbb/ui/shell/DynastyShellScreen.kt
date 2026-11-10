package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.balance.BalanceMedicalRedshirtRules
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.advance.SimWeekConfirmation
import dev.novux.dynasty.cbb.core.dashboard.DashboardAdvanceGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId
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
import dev.novux.dynasty.cbb.core.preseason.PreseasonSeasonStartPolicy
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.preseason.SeasonStartFocus
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.school.SchoolHubOverview
import dev.novux.dynasty.cbb.data.coach.CoachSecurityRepository
import dev.novux.dynasty.cbb.data.coach.SchoolStaffRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.data.chemistry.ChemistryRepository
import dev.novux.dynasty.cbb.data.discipline.DisciplineRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.ui.dashboard.DashboardRequiredTasksPanel
import dev.novux.dynasty.cbb.data.school.SchoolHubRepository
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.ui.dashboard.DashboardOptionalTasksPanel
import dev.novux.dynasty.cbb.ui.player.PlayerCardSheet
import dev.novux.dynasty.cbb.ui.player.PlayerInjuryRosterRow
import dev.novux.dynasty.cbb.ui.player.PlayerLoyaltyChips
import dev.novux.dynasty.cbb.ui.postgame.PostgameInjurySheet
import dev.novux.dynasty.cbb.ui.coach.AssistantCoachCardScreen
import dev.novux.dynasty.cbb.ui.coach.HeadCoachCardScreen
import dev.novux.dynasty.cbb.ui.coach.SchoolStaffTabPanel
import dev.novux.dynasty.cbb.ui.school.SchoolHubScreen
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellChrome
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellMenuAction
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellTab
import dev.novux.dynasty.cbb.ui.hubs.DynastyHubMenuCallbacks
import dev.novux.dynasty.cbb.ui.hubs.buildDynastyHubMenu
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
    data class HeadCoach(val card: HeadCoachCard) : ShellCoachView()
    data class Assistant(val card: AssistantCoachCard) : ShellCoachView()
}

@Composable
fun DynastyShellScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    scheduleRepository: PreseasonScheduleRepository,
    staffRepository: SchoolStaffRepository,
    schoolHubRepository: SchoolHubRepository,
    coachSecurityRepository: CoachSecurityRepository,
    rosterRepository: DynastyRosterRepository,
    disciplineRepository: DisciplineRepository,
    chemistryRepository: ChemistryRepository,
    userCalendarRepository: UserCalendarRepository,
    newsFeedRepository: NewsFeedRepository,
    hubMenuCallbacks: DynastyHubMenuCallbacks,
    onOpenDiscipline: (PendingDisciplineIncident) -> Unit,
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    var pendingAdvanceConfirmation by remember { mutableStateOf<SimWeekConfirmation?>(null) }
    var focus by remember { mutableStateOf<SeasonStartFocus?>(null) }
    var staffRoster by remember { mutableStateOf<SchoolStaffRoster?>(null) }
    var coachSecurity by remember { mutableStateOf<CoachSecuritySnapshot?>(null) }
    var userSchool by remember { mutableStateOf<SchoolHubOverview?>(null) }
    var coachView by remember { mutableStateOf<ShellCoachView>(ShellCoachView.Hub) }
    var rosterPreview by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var cardPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var rosterPlayers by remember { mutableStateOf<List<RosterPlayer>>(emptyList()) }
    var postgameEvents by remember { mutableStateOf<List<GameSimInjuryEvent>>(emptyList()) }
    var dismissedInjuryTasks by remember { mutableStateOf(setOf<DashboardTaskId>()) }
    var disciplinePending by remember { mutableStateOf<PendingDisciplineIncident?>(null) }
    var teamChemistry by remember { mutableStateOf<Int?>(null) }
    var schedulePlan by remember { mutableStateOf<PreseasonSchedulePlan?>(null) }
    var exhibitionCount by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(DynastyShellTab.Dashboard) }
    var fullRoster by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var newsHeadlines by remember { mutableStateOf<List<NewsHeadline>>(emptyList()) }
    val scope = rememberCoroutineScope()
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
    )

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex, loaded.weekCheckpoint) {
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
    }

    PlayerCardSheet(player = cardPlayer, onDismiss = { cardPlayer = null })
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
                                onHeadCoachClick = {
                                    coachView = ShellCoachView.HeadCoach(staffRoster!!.headCoach)
                                },
                                onAssistantClick = { id ->
                                    staffRoster!!.assistants.firstOrNull { it.staffId == id }?.let {
                                        coachView = ShellCoachView.Assistant(it)
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
                        onHeadCoachClick = { coachView = ShellCoachView.HeadCoach(roster.headCoach) },
                        onAssistantClick = { id ->
                            roster.assistants.firstOrNull { it.staffId == id }?.let {
                                coachView = ShellCoachView.Assistant(it)
                            }
                        },
                    )
                }
            }
            return
        }
        ShellCoachView.Hub -> Unit
    }

    val position = loaded.weekCheckpoint.activePosition
    val schoolName = loaded.summary.schoolDisplayName.orEmpty()
    val phaseLabel = CalendarWeekLabel.display(position)
    val shellMenu = buildDynastyHubMenu(
        position = position,
        callbacks = hubMenuCallbacks.copy(
            onOpenSchool = { userSchool?.let { coachView = ShellCoachView.School(it) } },
        ),
        leadingExtras = listOf(
            DynastyShellMenuAction(R.string.staff_open_tab) { coachView = ShellCoachView.Staff },
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
                DashboardRequiredTasksPanel(
                    tasks = dashboardTasks.required,
                    onOpenSigningDay = { },
                    onOpenDiscipline = { disciplinePending?.let(onOpenDiscipline) },
                )
                DashboardOptionalTasksPanel(
                    tasks = dashboardTasks.optional,
                    dismissedIds = dismissedInjuryTasks,
                    onDismiss = { dismissedInjuryTasks = dismissedInjuryTasks + it },
                    onDeepLink = { },
                    onStrategyOpen = onOpenStrategy,
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
                    )
                }
                pendingAdvanceConfirmation?.let { confirmation ->
                    SimWeekAdvanceDialog(
                        confirmation = confirmation,
                        onConfirm = {
                            pendingAdvanceConfirmation = null
                            scope.launch {
                                when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                                    is DynastyLoadResult.Success -> {
                                        loaded = result.loaded
                                        ActiveDynastySession.set(result.loaded)
                                        dismissedInjuryTasks = emptySet()
                                    }
                                    else -> Unit
                                }
                            }
                        },
                        onDismiss = { pendingAdvanceConfirmation = null },
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            if (DashboardAdvanceGate.blocksWeekAdvance(dashboardTasks.required)) {
                                return@launch
                            }
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
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !DashboardAdvanceGate.blocksWeekAdvance(dashboardTasks.required),
                ) {
                    Text(stringResource(R.string.dynasty_shell_advance_week))
                }
            }
        }
    }
}

@Composable
private fun NextGameCard(
    game: FirstPlayableGame,
    onViewOpponentSchool: () -> Unit,
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
        }
    }
}
