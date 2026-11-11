package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.ui.player.PlayerCardPreviewSheet
import dev.novux.dynasty.cbb.ui.player.PlayerCardScreen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.dashboard.DashboardAdvanceGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskGate
import dev.novux.dynasty.cbb.core.preseason.PreseasonFeatureTaskCompletionPolicy
import dev.novux.dynasty.cbb.core.preseason.PreseasonStartGate
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskProgress
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInvitesState
import dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonReportRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonSeasonStartRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.data.preseason.TournamentInvitesRepository
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellChrome
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellTab
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCyclePolicy
import dev.novux.dynasty.cbb.ui.hubs.DynastyHubMenuCallbacks
import dev.novux.dynasty.cbb.ui.hubs.buildDynastyHubMenu
import dev.novux.dynasty.cbb.ui.hubs.notifyRecruitingCycleClosed
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellCalendarTab
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellNewsTabPlaceholder
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellRosterTab
import kotlinx.coroutines.launch

@Composable
fun PreseasonScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    progressRepository: PreseasonProgressRepository,
    incomingClassSeedRepository: IncomingClassSeedRepository,
    tournamentInvitesRepository: TournamentInvitesRepository,
    scheduleRepository: PreseasonScheduleRepository,
    preseasonReportRepository: PreseasonReportRepository,
    seasonStartRepository: PreseasonSeasonStartRepository,
    onSeasonStarted: () -> Unit,
    hubMenuCallbacks: DynastyHubMenuCallbacks,
    rosterRepository: DynastyRosterRepository,
    userCalendarRepository: UserCalendarRepository,
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    var progress by remember { mutableStateOf<PreseasonTaskProgress?>(null) }
    var tournamentInvitesState by remember { mutableStateOf<TournamentInvitesState?>(null) }
    var selectedTab by remember { mutableStateOf(DynastyShellTab.Dashboard) }
    var fullRoster by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var cardPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var fullScreenPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var recruitingLedger by remember { mutableStateOf<RecruitingCycleLedger?>(null) }
    val context = LocalContext.current
    val app = context.requireDynastyApplication()
    val playerStatsRepository = app.playerStatsRepository
    val recruitingEconomyRepository = app.recruitingEconomyRepository
    val scope = rememberCoroutineScope()
    val position = loaded.weekCheckpoint.activePosition

    val coachSkillPointRepository = app.coachSkillPointRepository

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex, selectedTab) {
        progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
        tournamentInvitesState = tournamentInvitesRepository.loadState(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        )
        fullRoster = rosterRepository.listDynastyPlayers(loaded.dynastyId)
        recruitingLedger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
    }

    val schoolName = loaded.summary.schoolDisplayName.orEmpty()
    val phaseLabel = CalendarWeekLabel.display(position)
    val offseasonWeekForRecruiting = position.weekIndex.takeIf { position.phase == CalendarPhase.Offseason }
    val recruitingCycleClosed = RecruitingCyclePolicy.recruitingMenuTreatsCycleClosed(
        position.phase,
        offseasonWeekForRecruiting,
        recruitingLedger,
    )
    val menu = buildDynastyHubMenu(
        position = position,
        callbacks = hubMenuCallbacks,
        recruitingCycleClosed = recruitingCycleClosed,
        onRecruitingCycleClosed = { notifyRecruitingCycleClosed(context) },
    )

    PlayerCardPreviewSheet(
        player = cardPlayer,
        onDismiss = { cardPlayer = null },
        onViewMore = { player ->
            cardPlayer = null
            fullScreenPlayer = player
        },
        allowEdit = false,
    )

    fullScreenPlayer?.let { player ->
        PlayerCardScreen(
            player = player,
            schoolDisplayName = schoolName,
            seasonRecordLabel = "0-0",
            seasonIndex = loaded.metadata.seasonIndex,
            playerStatsRepository = playerStatsRepository,
            allowEdit = false,
            onBack = { fullScreenPlayer = null },
        )
        return
    }

    DynastyShellChrome(
        schoolDisplayName = schoolName,
        phaseWeekLabel = phaseLabel,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        menuActions = menu,
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
            DynastyShellTab.News -> DynastyShellNewsTabPlaceholder(padding)
            DynastyShellTab.Dashboard -> {
                val taskProgress = progress
                if (taskProgress == null) {
                    PreseasonDashboardLoadingBody(
                        contentPadding = padding,
                        seasonIndex = loaded.metadata.seasonIndex,
                    )
                } else {
                    val preseasonDashboard = DashboardTaskGate.preseason(taskProgress)
                    Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
        Text(
            text = SeasonMembershipYear.label(loaded.metadata.seasonIndex),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.preseason_checklist_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(PreseasonTaskId.entries) { taskId ->
                if (taskId == PreseasonTaskId.PreseasonReport &&
                    !taskProgress.isComplete(taskId)
                ) {
                    PreseasonReportPanel(
                        dynastyId = loaded.dynastyId,
                        seasonIndex = loaded.metadata.seasonIndex,
                        reportRepository = preseasonReportRepository,
                        progressRepository = progressRepository,
                        onMarkedRead = {
                            scope.launch {
                                progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    return@items
                }
                if (taskId == PreseasonTaskId.IncomingClassSeed && !taskProgress.isComplete(taskId)) {
                    IncomingClassSeedPanel(
                        dynastyId = loaded.dynastyId,
                        seasonIndex = loaded.metadata.seasonIndex,
                        seedRepository = incomingClassSeedRepository,
                        progressRepository = progressRepository,
                        onSeeded = {
                            scope.launch {
                                progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    return@items
                }
                if (taskId == PreseasonTaskId.TournamentInvites && !taskProgress.isComplete(taskId)) {
                    TournamentInvitesPanel(
                        dynastyId = loaded.dynastyId,
                        seasonIndex = loaded.metadata.seasonIndex,
                        invitesRepository = tournamentInvitesRepository,
                        progressRepository = progressRepository,
                        onResolved = {
                            scope.launch {
                                progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                                tournamentInvitesState = tournamentInvitesRepository.loadState(
                                    loaded.dynastyId,
                                    loaded.metadata.seasonIndex,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    return@items
                }
                if (taskId == PreseasonTaskId.ScheduleExhibitions &&
                    taskProgress.canComplete(taskId) &&
                    !taskProgress.isComplete(taskId)
                ) {
                    ExhibitionSchedulePanel(
                        dynastyId = loaded.dynastyId,
                        seasonIndex = loaded.metadata.seasonIndex,
                        scheduleRepository = scheduleRepository,
                        progressRepository = progressRepository,
                        onSaved = {
                            scope.launch {
                                progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    return@items
                }
                if (taskId == PreseasonTaskId.ScheduleRegularSeason &&
                    taskProgress.canComplete(taskId) &&
                    !taskProgress.isComplete(taskId)
                ) {
                    RegularSeasonSchedulePanel(
                        dynastyId = loaded.dynastyId,
                        seasonIndex = loaded.metadata.seasonIndex,
                        scheduleRepository = scheduleRepository,
                        progressRepository = progressRepository,
                        onSaved = {
                            scope.launch {
                                progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    return@items
                }
                if (taskId == PreseasonTaskId.PreseasonWorkouts && !taskProgress.isComplete(taskId)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        PreseasonWorkoutsPanel(
                            dynastyId = loaded.dynastyId,
                            seasonIndex = loaded.metadata.seasonIndex,
                            rosterRepository = rosterRepository,
                            progressRepository = progressRepository,
                            onComplete = {
                                scope.launch {
                                    progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }
                    return@items
                }
                if (taskId == PreseasonTaskId.CoachUpgrades && !taskProgress.isComplete(taskId)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        PreseasonCoachUpgradesPanel(
                            dynastyId = loaded.dynastyId,
                            seasonIndex = loaded.metadata.seasonIndex,
                            coachSkillPointRepository = coachSkillPointRepository,
                            progressRepository = progressRepository,
                            onOpenStrategy = hubMenuCallbacks.onOpenStrategy,
                            onComplete = {
                                scope.launch {
                                    progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }
                    return@items
                }
                if (taskId == PreseasonTaskId.RankingsReview && !taskProgress.isComplete(taskId)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        PreseasonRankingsReviewPanel(
                            onOpenRankings = hubMenuCallbacks.onOpenRankings,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }
                    return@items
                }
                val done = taskProgress.isComplete(taskId)
                val enabled = taskProgress.canComplete(taskId) && !done
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (done) {
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        CardDefaults.cardColors()
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preseasonTaskTitle(taskId),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            if (!taskProgress.schedulingUnlocked() &&
                                (taskId == PreseasonTaskId.ScheduleExhibitions ||
                                    taskId == PreseasonTaskId.ScheduleRegularSeason)
                            ) {
                                Text(
                                    text = stringResource(R.string.preseason_task_locked_invites),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                            if (taskProgress.schedulingUnlocked() &&
                                taskId == PreseasonTaskId.ScheduleRegularSeason
                            ) {
                                tournamentInvitesState?.let { invites ->
                                    Text(
                                        text = stringResource(
                                            R.string.preseason_rs_builder_slots,
                                            invites.slotBudget.builderSlots,
                                            invites.slotBudget.totalRsCap,
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                        if (done) {
                            Text(
                                text = stringResource(R.string.preseason_task_done),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else if (PreseasonFeatureTaskCompletionPolicy.allowsGenericCompleteButton(taskId)) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        progress = progressRepository.completeTask(
                                            loaded.dynastyId,
                                            loaded.metadata.seasonIndex,
                                            taskId,
                                        )
                                    }
                                },
                                enabled = enabled,
                            ) {
                                Text(stringResource(R.string.preseason_task_complete))
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    when (val result = seasonStartRepository.startSeason(loaded.slotIndex)) {
                        is DynastyLoadResult.Success -> {
                            loaded = result.loaded
                            ActiveDynastySession.set(result.loaded)
                            onSeasonStarted()
                        }
                        else -> Unit
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = PreseasonStartGate.canStartSeason(taskProgress) &&
                !DashboardAdvanceGate.blocksWeekAdvance(preseasonDashboard.required),
        ) {
            Text(stringResource(R.string.preseason_start_season))
        }
            }
                }
            }
        }
    }
}

@Composable
private fun PreseasonDashboardLoadingBody(
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    seasonIndex: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        Text(
            text = SeasonMembershipYear.label(seasonIndex),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
            Text(
                text = stringResource(R.string.preseason_loading_checklist),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        repeat(4) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.55f))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.35f))
                }
            }
        }
    }
}

@Composable
private fun preseasonTaskTitle(taskId: PreseasonTaskId): String = when (taskId) {
    PreseasonTaskId.IncomingClassSeed -> stringResource(R.string.preseason_task_incoming_class)
    PreseasonTaskId.PreseasonWorkouts -> stringResource(R.string.preseason_task_workouts)
    PreseasonTaskId.CoachUpgrades -> stringResource(R.string.preseason_task_coach_upgrades)
    PreseasonTaskId.RankingsReview -> stringResource(R.string.preseason_task_rankings)
    PreseasonTaskId.PreseasonReport -> stringResource(R.string.preseason_task_report)
    PreseasonTaskId.TournamentInvites -> stringResource(R.string.preseason_task_tournament_invites)
    PreseasonTaskId.ScheduleExhibitions -> stringResource(R.string.preseason_task_exhibitions)
    PreseasonTaskId.ScheduleRegularSeason -> stringResource(R.string.preseason_task_regular_schedule)
}
